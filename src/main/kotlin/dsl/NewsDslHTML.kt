package com.fintech.kotlin.dsl
import org.example.News

fun prettyPrintHTML(list: Collection<News>) = html {
    head {
        title { +"PRETTY PRINT LIST NEWS" }
    }
    body {
        h1 { +"PRETTY PRINT LIST NEWS" }
        for (el in list) {
            div {
                font(color = "#fa8e47") { p { +"-------------------------------------------------------------------" } }
                font(color = "#fa8e47") { h2 { +"NEWS" } }
                u { b { +"Id:" } }
                p { +el.id.toString() }
                u { b { +"Title:" } }
                p { +el.title }
                u { b { +"Description:" } }
                p { +el.description }
                u { b { +"Site URL:" } }
                p { a(href = el.site_url) { +"url" } }
                u { b { +"Place id:" } }
                p { +el.place?.id.toString() }
                u { b { +"Favorites count:" } }
                p { +el.favorites_count.toString() }
                u { b { +"Comments count:" } }
                p { +el.comments_count.toString() }
                u { b { +"Rating:" } }
                p { +el.rating.toString() }
                u { b { +"Publication date:" } }
                p { +el.publication_date.toString() }
            }
        }
    }
}

interface ElementHTML {
    fun render(builder: StringBuilder, indent: String)
}

class TextElementHTML(val text: String) : ElementHTML {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text")
    }
}

@DslMarker
annotation class HtmlTagMarker

@HtmlTagMarker
abstract class Tag(val name: String) : ElementHTML {
    val children = arrayListOf<ElementHTML>()
    val attributes = hashMapOf<String, String>()

    protected fun <T : ElementHTML> initTagHTML(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent<$name${renderAttributes()}>")
        for (c in children) {
            c.render(builder, indent + "  ")
        }
        builder.append("$indent</$name>")
    }

    private fun renderAttributes(): String {
        val builder = StringBuilder()
        for ((attr, value) in attributes) {
            builder.append(" $attr=\"$value\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElementHTML(this))
    }
}

class HTML : TagWithText("html") {
    fun head(init: Head.() -> Unit) = initTagHTML(Head(), init)

    fun body(init: Body.() -> Unit) = initTagHTML(Body(), init)
}

class Head : TagWithText("head") {
    fun title(init: TitleHTML.() -> Unit) = initTagHTML(TitleHTML(), init)
}

class Body : TagWithText("body") {
    fun div(init: Div.() -> Unit) = initTagHTML(Div(), init)
    fun h1(init: H1.() -> Unit) = initTagHTML(H1(), init)
}

class TitleHTML : TagWithText("title")

abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTagHTML(B(), init)
    fun u(init: U.() -> Unit) = initTagHTML(U(), init)
    fun p(init: P.() -> Unit) = initTagHTML(P(), init)
    fun h2(init: H2.() -> Unit) = initTagHTML(H2(), init)
    fun div(init: Div.() -> Unit) = initTagHTML(Div(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTagHTML(A(), init)
        a.href = href
    }

    fun font(color: String, init: Font.() -> Unit) {
        val font = initTagHTML(Font(), init)
        font.color = color
    }
}

class B : BodyTag("b")
class P : BodyTag("p")
class H1 : BodyTag("h1")
class H2 : BodyTag("h2")
class Div : BodyTag("div")
class U : BodyTag("u")

class A : BodyTag("a") {
    var href: String
        get() = attributes["href"]!!
        set(value) {
            attributes["href"] = value
        }
}

class Font : BodyTag("font") {
    var color: String
        get() = attributes["color"]!!
        set(value) {
            attributes["color"] = value
        }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}