/**
 * Copyright (c) 2011 MNCC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author http://www.mncc.fr
 */
package fr.mncc.gwttoolbox.rpc.server.filters;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UrlFetchWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * For AJAX crawlable websites
 */
public final class CrawlFilter implements Filter {

  public final static String SCHEME = "http";
  public final static long PUMP_TIME = 5000;
  private final static Logger logger_ = Logger.getLogger(CrawlFilter.class.getCanonicalName());
  private final static ThreadLocal<WebClient> webClient_ = new ThreadLocal<WebClient>() {
    @Override
    protected synchronized WebClient initialValue() {
      WebClient result = new WebClient(BrowserVersion.FIREFOX_10);
      result.setWebConnection(new UrlFetchWebConnection(result));
      return result;
    }
  };

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException {

    HttpServletRequest req = (HttpServletRequest) request;
    String queryString = req.getQueryString();
    logger_.log(Level.INFO, "queryString=" + queryString);
    logger_.log(Level.INFO, "requestURL=" + req.getRequestURL());

    if (queryString != null && queryString.contains("_escaped_fragment_=")) {

      String uri = req.getRequestURI();
      int port = req.getServerPort();
      String domain = req.getServerName();

      // rewrite the URL back to the original #! version
      // remember to unescape any %XX characters
      String url_with_hash_fragment = uri + rewriteQueryString(queryString);
      logger_.log(Level.INFO, "url_with_hash_fragment=" + url_with_hash_fragment);

      // use the headless browser to obtain an HTML snapshot
      URL url = new URL(SCHEME, domain, port, url_with_hash_fragment);
      logger_.log(Level.INFO, "url=" + url.toString());
      HtmlPage page = webClient_.get().getPage(url);

      // important! Give the headless browser enough time to execute JavaScript
      // The exact time to wait may depend on your application.
      webClient_.get().waitForBackgroundJavaScript(PUMP_TIME);

      // GAE hack because its single threaded
      webClient_.get().getJavaScriptEngine().pumpEventLoop(PUMP_TIME);

      logger_.log(Level.INFO, "page.asXml()=" + page.asXml());

      response.setContentType("text/html;charset=UTF-8");
      ServletOutputStream out = response.getOutputStream();
      out.println(page.asXml());
    } else {
      try {
        // not an _escaped_fragment_ URL, so move up the chain of servlet (filters)
        chain.doFilter(request, response);
      } catch (ServletException e) {
        logger_.log(Level.SEVERE, e.toString());
      }
    }
  }

  /**
   * Performs clean up of WebClient
   */
  @Override
  public void destroy() {
    if (webClient_.get() != null) {
      webClient_.get().closeAllWindows();
    }
  }

  /**
   * Initialise com.gargoylesoftware.htmlunit.WebClient
   */
  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

  public String rewriteQueryString(String url_with_escaped_fragment) {
    try {
      String decoded = URLDecoder.decode(url_with_escaped_fragment, "UTF-8");

      // this helps run on development mode
      String gwt = decoded.replace("gwt", "?gwt");
      String unescapedAmp = gwt.replace("&_escaped_fragment_=", "#!");
      String result = unescapedAmp.replace("_escaped_fragment_=", "#!");
      return result;
    } catch (UnsupportedEncodingException e) {
      logger_.log(Level.SEVERE, e.toString());
      return "";
    }
  }
}
