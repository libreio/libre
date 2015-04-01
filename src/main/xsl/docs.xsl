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
        <form method="post" action="{links/link[@rel='mkdir']/@href}">
            <input name="name" type="text" placeholder="New directory name"/>
            <input name="create" type="submit"/>
        </form>
        <hr/>
        <p>
            <xsl:text>My docs:</xsl:text>
        </p>
        <form method="post" action="{links/link[@rel='upload']/@href}"
            enctype="multipart/form-data">
            <input name="file" type="file"/>
            <input name="upload" type="submit"/>
        </form>
        <xsl:apply-templates select="docs"/>
    </xsl:template>
    <xsl:template match="docs[doc]">
        <ul>
            <xsl:apply-templates select="doc"/>
        </ul>
    </xsl:template>
    <xsl:template match="doc">
        <li>
            <xsl:value-of select="name"/>
            <xsl:text> </xsl:text>
            <a href="{read}">read</a>
            <xsl:text> | </xsl:text>
            <a href="{delete}">delete</a>
            <xsl:apply-templates select="friends"/>
        </li>
    </xsl:template>
    <xsl:template match="friends">
        <ul>
            <xsl:apply-templates select="friend"/>
            <li>
                <form action="{../add-friend}" method="post">
                    <input name="friend" placeholder="share with..."/>
                    <input type="submit"/>
                </form>
            </li>
        </ul>
    </xsl:template>
    <xsl:template match="friend">
        <li>
            <xsl:value-of select="name"/>
            <xsl:text> </xsl:text>
            <a href="{eject}">eject</a>
        </li>
    </xsl:template>
</xsl:stylesheet>
