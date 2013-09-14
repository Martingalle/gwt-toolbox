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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Stopwatch;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * For AJAX crawlable websites
 */
public class CrawlFilter implements Filter {

  public final static String SCHEME = "http";
  public final static long PUMP_TIME = 500;
  private final static Logger logger_ = Logger.getLogger(CrawlFilter.class.getCanonicalName());

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException {

    HttpServletRequest req = (HttpServletRequest) request;
    String queryString = req.getQueryString();

    logger_.log(Level.INFO, "queryString=" + queryString);
    logger_.log(Level.INFO, "requestURL=" + req.getRequestURL());

    if (queryString != null && queryString.contains("_escaped_fragment_=")) {

      final String uri = req.getRequestURI();
      final int port = req.getServerPort();
      final String domain = req.getServerName();

      // rewrite the URL back to the original #! version
      // remember to unescape any %XX characters
      final String url_with_hash_fragment = uri + rewriteQueryString(queryString);

      logger_.log(Level.INFO, "url_with_hash_fragment=" + url_with_hash_fragment);

      // and use the headless browser to obtain an HTML snapshot
      final URL url = new URL(SCHEME, domain, port, url_with_hash_fragment);
      final WebRequest webRequest = new WebRequest(url);

      logger_.log(Level.INFO, "url=" + url.toString());

      String staticSnapshotHtml = getPageFromCache(url.toString());
      if (staticSnapshotHtml == null || staticSnapshotHtml.isEmpty()) {

        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
        webClient.setAjaxController(new AjaxController() {

          @Override
          public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
            return true;
          }
        });
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(0);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        webClient.setWebConnection(new UrlFetchWebConnection(webClient) {
          @Override
          public WebResponse getResponse(WebRequest request) throws IOException {

            final URL url = request.getUrl();
            final String file = url.getFile();

            if (!ignoreFile(file)) {
              logger_.log(Level.INFO, "*** Loaded : " + file);
              return super.getResponse(request);
            }

            logger_.log(Level.INFO, "*** Ignored : " + file);
            return new StringWebResponse("", request.getUrl());
          }
        });

        stopwatch.stop();
        logger_.log(Level.INFO, "doFilter() : webClient_.get().setWebConnection() --> "
            + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

        stopwatch.reset();
        stopwatch.start();

        final HtmlPage page = webClient.getPage(webRequest);

        stopwatch.stop();
        logger_.log(Level.INFO, "doFilter() : webClient_.get().getPage() --> "
            + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

        // Important! Give the headless browser enough time to execute JavaScript
        // The exact time to wait may depend on your application.
        webClient.getJavaScriptEngine().pumpEventLoop(PUMP_TIME);

        staticSnapshotHtml = getPage(page, url.toString());
        webClient.closeAllWindows();

        savePageToCache(url.toString(), staticSnapshotHtml);
      }

      logger_.log(Level.INFO, "page.asXml()=" + staticSnapshotHtml);

      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println(staticSnapshotHtml);
      out.flush();
      out.close();
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

  /**
   * Tell HtmlUnit whether to download or not a given file.
   * 
   * @param file
   * @return true if HtmlUnit should download the file, false otherwise.
   */
  protected boolean ignoreFile(String file) {
    return false;
  }

  protected String getPage(HtmlPage page, String url) {
    return page.asXml();
  }

  protected void savePageToCache(String url, String page) {

  }

  protected String getPageFromCache(String url) {
    return "";
  }
}
