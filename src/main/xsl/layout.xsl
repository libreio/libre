<?xml version="1.0"?>
<!--
 * Copyright (c) 2015, nerodesk.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the nerodesk.com nor
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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:template match="/page">
        <html lang="en">
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <meta name="description" content="nerodesk"/>
                <meta name="keywords" content="cloud file storage"/>
                <meta name="author" content="nerodesk.com"/>
                <link rel="stylesheet" type="text/css" media="all" href="//yegor256.github.io/tacit/tacit.min.css"/>
                <link rel="stylesheet" type="text/css" media="all" href="/css/main.css?{version/revision}"/>
                <xsl:apply-templates select="." mode="head"/>
            </head>
            <body>
                <section style="height:100%">
                    <xsl:if test="identity">
                        <header>
                            <nav>
                                <ul>
                                    <li>
                                        <img style="width:192px;height:42px;"
                                            src="/images/logo.png"/>
                                    </li>
                                </ul>
                            </nav>
                            <nav role="navigation" class="navbar navbar-center">
                                <ul>
                                    <xsl:apply-templates select="identity"/>
                                    <xsl:apply-templates select="user"/>
                                    <xsl:apply-templates select="flash"/>
                                    <li title="total amount of bytes stored in your account">
                                        <!-- @todo #102:30min This value is hardcoded for now but should be connected to Docs.size() value and present total amount of bytes in user friendly form stored in account. -->
                                        <xsl:text>[storage total]</xsl:text>
                                    </li>
                                </ul>
                            </nav>
                        </header>
                    </xsl:if>
                    <xsl:apply-templates select="." mode="body"/>
                    <xsl:if test="identity">
                        <footer>
                            <nav>
                                <ul>
                                    <li>
                                        <xsl:text>&#169; Nerodesk.com, 2015</xsl:text>
                                    </li>
                                    <li>
                                        <xsl:text>Source code is available at </xsl:text>
                                        <a href="https://github.com/nerodesk/nerodesk">GitHub</a>
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
            <!--
            <xsl:text>$</xsl:text>
            <xsl:value-of select="format-number(balance div 100, '0.00')"/>
            -->
            <!--
            @todo #118:30min Create a page account.xsl that will display all of
             the transactions performed on the account and make the above
             balance a link to the account page.
            -->
        </li>
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
        <li title="{urn}">
            <xsl:value-of select="name|login"/>
        </li>
        <li>
            <a title="log out" href="{/page/links/link[@rel='takes:logout']/@href}">
                <xsl:text>logout</xsl:text>
            </a>
        </li>
    </xsl:template>
    <xsl:template match="identity[urn='urn:test:1']">
        <li>
            <xsl:text>tester</xsl:text>
        </li>
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
