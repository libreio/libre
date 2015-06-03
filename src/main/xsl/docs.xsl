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
    <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes" />
    <xsl:include href="/xsl/layout.xsl"/>
    <xsl:template match="page" mode="head">
        <title>
            <xsl:text>docs</xsl:text>
        </title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <article>
            <!--<form method="post" action="{links/link[@rel='mkdir']/@href}">
                <input name="name" type="text" placeholder="New directory name"/>
                <input name="create" type="submit"/>
            </form>
            <hr/>
            -->
            <h2>
                <xsl:text>My Documents</xsl:text>
            </h2>
            <form method="post" action="{links/link[@rel='upload']/@href}"
                enctype="multipart/form-data"
                onSubmit="if(document.getElementById('fileinput').value.trim() == '') return false;">
                <input id="fileinput" name="file" type="file"/>
                <button type="submit">Upload</button>
            </form>
            <xsl:apply-templates select="docs"/>
            <p>
            <small>
             <div align="center">
              <xsl:text>
                Nerodesk is currently in beta testing mode.
              </xsl:text>
             </div>
            </small>
            </p>
        </article>
    </xsl:template>
    <xsl:template match="docs[doc]">
        <table style="width:100%">
            <thead>
                <tr>
                    <th><xsl:text>File</xsl:text></th>
                    <th style="width:1%"><xsl:text>Options</xsl:text></th>
                    <th style="width:1%"><xsl:text>Permissions</xsl:text></th>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="doc"/>
            </tbody>
        </table>
    </xsl:template>
    <xsl:template match="doc">
        <tr>
            <td>
                <a href="{links/link[@rel='read']/@href}" style="display:block">
                    <xsl:value-of select="name"/>
                </a>
                <small>
                    <xsl:text>[</xsl:text>
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
                    </xsl:choose>
                    <xsl:text>] </xsl:text>
                    <xsl:value-of select="created"/>
                </small>
            </td>
            <td>
                <a href="{links/link[@rel='delete']/@href}">delete</a>
                <a href="{links/link[@rel='short']/@href}" style="display:block">
                    share
                </a>
            </td>
            <td>
                <xsl:apply-templates select="visibility"/>
            </td>
        </tr>
    </xsl:template>
<!--    <xsl:template match="friends">
        <xsl:apply-templates select="friend"/>
        <form action="{../links/link[@rel='add-friend']/@href}" method="post">
            <input name="friend" type="text" placeholder="email"/>
            <button>Share</button>
        </form>
    </xsl:template>

    <xsl:template match="friend">
        <span>
            <xsl:value-of select="name"/>
            <xsl:text> (</xsl:text>
            <a href="{eject}">stop</a>
            <xsl:text>) </xsl:text>
        </span>
    </xsl:template>
-->
    <xsl:template match="visibility">
        <form action="{../links/link[@rel='set-visibility']/@href}" method="post">
            <xsl:element name="input">
                <xsl:attribute name="type">checkbox</xsl:attribute>
                <xsl:attribute name="name">visibility</xsl:attribute>
                <xsl:attribute name="value">Public</xsl:attribute>
                <xsl:if test=".='true'">
                    <xsl:attribute name="checked">
                    </xsl:attribute>
                </xsl:if>
                <xsl:attribute name="onChange">this.form.submit()</xsl:attribute>
                <xsl:text>Public</xsl:text>
            </xsl:element>
        </form>
    </xsl:template>
</xsl:stylesheet>
