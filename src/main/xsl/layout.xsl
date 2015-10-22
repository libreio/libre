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
 <xsl:template match="/page">
  <html lang="en">
   <head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="libre"/>
    <meta name="keywords" content="Simple AWS gatway for storing and sharing files"/>
    <meta name="author" content="libre.io"/>
    <link rel="stylesheet" type="text/css" media="all" href="//yegor256.github.io/tacit/tacit.min.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/main.css"/>
    <xsl:apply-templates select="." mode="head"/>
   </head>
   <body>
    <section style="height:100%">
     <xsl:if test="identity">
      <header>
       <nav>
        <aside id="logo">
         <xsl:text>libre.io</xsl:text>
        </aside>
       </nav>
      </header>
     </xsl:if>
     <xsl:apply-templates select="." mode="body"/>
     <br/>
     <xsl:if test="identity">
      <nav role="navigation">
       <article>
        <table>
         <thead>
          <tr>
           <th></th>
           <th>User</th>
           <th>Storage</th>
           <th>Action</th>
          </tr>
         </thead>
         <tbody>
          <tr>
           <td>
            <xsl:text>logged in as</xsl:text>
           </td>
           <td>
            <xsl:apply-templates select="identity"/>
           </td>
           <td>
            <xsl:apply-templates select="account"/>
           </td>
           <td>
            <xsl:apply-templates select="flash"/>
           </td>
          </tr>
         </tbody>
        </table>
       </article>
      </nav>
     </xsl:if>
     <xsl:if test="identity">
      <footer>
       <nav>
        <ul>
         <li>
          <xsl:text>libre.io</xsl:text>
         </li>
         <li>
          <xsl:text>Source code is available at </xsl:text>
          <a href="https://github.com/libre/libre">GitHub</a>
         </li>
        </ul>
       </nav>
      </footer>
     </xsl:if>
    </section>
    <xsl:apply-templates select="version"/>
   </body>
  </html>
 </xsl:template>
 <xsl:template match="version">
  <aside id="version">
   <nav>
    <ul>
     <li title="currently deployed version is {name}">
      <xsl:value-of select="name"/>
     </li>
     <li title="server time to build this page">
      <xsl:attribute name="style">
                            <xsl:text>color:</xsl:text>
                            <xsl:choose>
                                <xsl:when test="/page/millis &gt; 5000">
                                    <xsl:text>red</xsl:text>
                                </xsl:when>
                                <xsl:when test="/page/millis &gt; 1000">
                                    <xsl:text>orange</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>inherit</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
      <xsl:call-template name="millis">
       <xsl:with-param name="millis" select="/page/millis"/>
      </xsl:call-template>
     </li>
     <li title="server load average">
      <xsl:attribute name="style">
                            <xsl:text>color:</xsl:text>
                            <xsl:choose>
                                <xsl:when test="/page/@sla &gt; 6">
                                    <xsl:text>red</xsl:text>
                                </xsl:when>
                                <xsl:when test="/page/@sla &gt; 3">
                                    <xsl:text>orange</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>inherit</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
      <xsl:value-of select="/page/@sla"/>
     </li>
    </ul>
   </nav>
  </aside>
 </xsl:template>
 <xsl:template match="user">
  <li title="current user balance">
  </li>
 </xsl:template>
 <xsl:template match="account">
  <nav role="navigation">
   <ul>
    <li title="account size">
     <xsl:choose>
      <xsl:when test="size &gt;= 1073741824">
       <xsl:value-of select="format-number(size div 1073741824, '#,###')"/>
       <xsl:text>Gb</xsl:text>
      </xsl:when>
      <xsl:when test="size &gt;= 1048576">
       <xsl:value-of select="format-number(size div 1048576, '#,###')"/>
       <xsl:text>Mb</xsl:text>
      </xsl:when>
      <xsl:when test="size &gt;= 1024">
       <xsl:value-of select="format-number(size div 1024, '#,###')"/>
       <xsl:text>Kb</xsl:text>
      </xsl:when>
      <xsl:when test="size &gt; 0 and size &lt; 1024">
       <xsl:value-of select="format-number(size div 0, '#,###')"/>
       <xsl:text>bytes</xsl:text>
      </xsl:when>
      <xsl:otherwise>
       <xsl:text>0 bytes</xsl:text>
      </xsl:otherwise>
     </xsl:choose>
    </li>
   </ul>
  </nav>
 </xsl:template>
 <xsl:template match="flash">
  <div>
   <xsl:attribute name="style">
                <xsl:text>color:</xsl:text>
                <xsl:choose>
                    <xsl:when test="level = 'INFO'">
                        <xsl:text>#348C62</xsl:text>
                    </xsl:when>
                    <xsl:when test="level = 'WARNING'">
                        <xsl:text>orange</xsl:text>
                    </xsl:when>
                    <xsl:when test="level = 'SEVERE'">
                        <xsl:text>red</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>inherit</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
   <xsl:value-of select="message"/>
   <xsl:if test="msec &gt; 0">
    <xsl:text> (in </xsl:text>
    <xsl:call-template name="millis">
     <xsl:with-param name="millis" select="msec"/>
    </xsl:call-template>
    <xsl:text>)</xsl:text>
   </xsl:if>
  </div>
 </xsl:template>
 <xsl:template match="identity[urn!='urn:test:1']">
  <ul>
   <li title="{urn}">
    <xsl:value-of select="name|login"/>
   </li>
   <li>
    <a title="log out" href="{/page/links/link[@rel='takes:logout']/@href}">
     <xsl:text>logout</xsl:text>
    </a>
   </li>
  </ul>
 </xsl:template>
 <xsl:template match="identity[urn='urn:test:1']">
  <xsl:text>libre.io test acct</xsl:text>
 </xsl:template>
 <xsl:template name="millis">
  <xsl:param name="millis"/>
  <xsl:choose>
   <xsl:when test="not($millis)">
    <xsl:text>?</xsl:text>
   </xsl:when>
   <xsl:when test="$millis &gt; 60000">
    <xsl:value-of select="format-number($millis div 60000, '0')"/>
    <xsl:text>min</xsl:text>
   </xsl:when>
   <xsl:when test="$millis &gt; 1000">
    <xsl:value-of select="format-number($millis div 1000, '0.0')"/>
    <xsl:text>s</xsl:text>
   </xsl:when>
   <xsl:otherwise>
    <xsl:value-of select="format-number($millis, '0')"/>
    <xsl:text>ms</xsl:text>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>
</xsl:stylesheet>
