<?xml version="1.0"?>
<!--
 * Copyright (c) 2016, libre.io
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the libre.io nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="1.0">
 <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
 <xsl:include href="/xsl/layout.xsl"/>
 <xsl:template match="page" mode="head">
  <title>
   <xsl:text>libre.io</xsl:text>
  </title>
 </xsl:template>
 <xsl:template match="page" mode="body">
  <div id="center">
   <div>
    <p>
     <img style="width:240px;" src="/images/logo.png"/>
    </p>
    <p>
     <xsl:text>Login with:</xsl:text>
    </p>
    <nav>
     <ul>
      <li>
       <a title="facebook" href="{links/link[@rel='takes:facebook']/@href}">
        <img src="/svg/facebook.svg" alt="facebook icon"/>
       </a>
      </li>
      <li>
       <a title="github" href="{links/link[@rel='takes:github']/@href}">
        <img src="/svg/github.svg" alt="github icon"/>
       </a>
      </li>
      <li>
       <a title="twitter" href="{links/link[@rel='takes:twitter']/@href}">
        <img src="/svg/twitter.svg" alt="twitter icon"/>
       </a>
      </li>
      <li>
       <a title="google+" href="{links/link[@rel='takes:google']/@href}">
        <img src="/svg/google.svg" alt="google icon"/>
       </a>
      </li>
     </ul>
    </nav>
   </div>
  </div>
 </xsl:template>
</xsl:stylesheet>
