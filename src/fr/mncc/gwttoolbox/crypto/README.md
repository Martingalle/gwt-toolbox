gwt-crypto
==========

Because you do not always need strong cryptography.

Dependencies
============

* [Google Guava](http://code.google.com/p/guava-libraries/) 13.0 or above

What is inside ?
================

Shared :
* The [CubeHash](http://en.wikipedia.org/wiki/CubeHash) hashing algorithm : 
 * [fr.mncc.gwttoolbox.crypto.shared.CubeHash](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/crypto/shared/CubeHash.java)
* The [Rc4](http://en.wikipedia.org/wiki/RC4) stream cipher algorithm : 
 * [fr.mncc.gwttoolbox.crypto.shared.Rc4](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/crypto/shared/Rc4.java)
* An utility class to generate random strings : 
 * [fr.mncc.gwttoolbox.crypto.shared.RandomString](hhttps://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/crypto/shared/RandomString.java)

Client :
* Thin wrapper for easy access to the algorithms :
 * [fr.mncc.gwttoolbox.crypto.client.CryptoApi](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/crypto/client/CryptoApi.java)

Server :
* Thin wrapper for easy access to the algorithms :
 * [fr.mncc.gwttoolbox.crypto.server.CryptoApi](https://github.com/csavelief/gwt-toolbox/blob/master/src/fr/mncc/gwttoolbox/crypto/server/CryptoApi.java)

License : MIT
=============

Copyright (c) 2011 [MNCC](http://www.mncc.fr/)
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

