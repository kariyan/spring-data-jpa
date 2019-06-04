package com.example.blog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HtmlController(@Autowired val articleRepository: ArticleRepository,
                     @Autowired val blogProperties: BlogProperties) {

    @GetMapping("/")
    fun blog(model: Model): String {

        model["title"] = blogProperties.title
        model["banner"] = blogProperties.banner
        model["articles"] = articleRepository.findAllByOrderByAddedAtDesc().map {
            it.render()
        }
        return "blog"
    }

    @GetMapping("/article/{slug}")
    fun article(@PathVariable slug: String, model: Model): String {
        val article = articleRepository.findBySlug(slug)?.render()
                ?: throw IllegalArgumentException("Wrong article slug provided!")
        model["title"] = article.title
        model["article"] = article

        return "article"
    }
}

private fun Article.render() = RenderedArticle(
        slug,
        title,
        headline,
        content,
        author,
        addedAt.format()
)

data class RenderedArticle(
        val slug: String,
        val title: String,
        val headline: String,
        val content: String,
        val author: User,
        val addedAt: String
)
