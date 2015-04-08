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
        <h2>
            <xsl:text>My Documents</xsl:text>
        </h2>
        <form method="post" action="{links/link[@rel='upload']/@href}"
            enctype="multipart/form-data">
            <input name="file" type="file"/>
            <button type="submit">Upload</button>
        </form>
        <xsl:apply-templates select="docs"/>
        <p>
            <xsl:text>
                At the moment maximum document size is 10Mb.
                The system is still in beta testing mode,
                please excuse minor defects.
            </xsl:text>
        </p>
    </xsl:template>
    <xsl:template match="docs[doc]">
        <table style="width:100%">
            <thead>
                <tr>
                    <td><xsl:text>File</xsl:text></td>
                    <td><xsl:text>Options</xsl:text></td>
                    <td><xsl:text>Friends</xsl:text></td>
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
                <a href="{read}" style="display:block">
                    <xsl:value-of select="name"/>
                </a>
                <small>
                    <xsl:value-of select="size"/>
                    <xsl:text> bytes, </xsl:text>
                    <xsl:text>created on </xsl:text>
                    <!--
                    @todo #101:30min Creation date of document should be displayed using
                     ISO_8601 combined date time and timezone
                     (e.g. 2007-04-05T12:30-02:00). Right now it is just a unix
                     timestamp.
                    -->
                    <xsl:value-of select="created"/>
                </small>
            </td>
            <td>
                <a href="{delete}">delete</a>
            </td>
            <td>
                <xsl:apply-templates select="friends"/>
            </td>
        </tr>
    </xsl:template>
    <xsl:template match="friends">
        <xsl:apply-templates select="friend"/>
        <form action="{../add-friend}" method="post">
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
</xsl:stylesheet>
