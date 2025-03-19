package ai.sionic.mcpdemo

import ai.sionic.mcpdemo.model.ShortenUrlRequest
import ai.sionic.mcpdemo.model.ShortenUrlResponse
import ai.sionic.mcpdemo.model.UrlDto
import csstype.*
import emotion.react.css
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLInputElement
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import react.useEffect
import react.useState

private val scope = MainScope()
private val jsonFormat = Json { ignoreUnknownKeys = true }

val App = FC<Props> {
    var urls by useState(emptyList<UrlDto>())
    var inputUrl by useState("")
    var isLoading by useState(false)
    var message by useState<String?>(null)
    
    fun fetchUrls() {
        scope.launch {
            isLoading = true
            try {
                val response = window.fetch("/api/shorten/urls").await()
                if (response.ok) {
                    val text = response.text().await()
                    urls = jsonFormat.decodeFromString(text)
                } else {
                    message = "URL 목록을 불러오는데 실패했습니다."
                }
            } catch (e: Exception) {
                console.error("Error fetching URLs", e)
                message = "오류: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun shortenUrl() {
        if (inputUrl.isEmpty()) {
            message = "URL을 입력해주세요."
            return
        }
        
        scope.launch {
            isLoading = true
            try {
                val requestBody = ShortenUrlRequest(inputUrl)
                val response = window.fetch(
                    "/api/shorten",
                    RequestInit(
                        method = "POST",
                        headers = js("{\"Content-Type\": \"application/json\"}"),
                        body = jsonFormat.encodeToString(requestBody)
                    )
                ).await()
                
                if (response.ok) {
                    val text = response.text().await()
                    val result: ShortenUrlResponse = jsonFormat.decodeFromString(text)
                    message = "URL이 성공적으로 단축되었습니다: ${result.shortUrl}"
                    inputUrl = ""
                    fetchUrls()
                } else {
                    message = "URL 단축에 실패했습니다."
                }
            } catch (e: Exception) {
                console.error("Error shortening URL", e)
                message = "오류: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun deleteUrl(shortKey: String) {
        scope.launch {
            isLoading = true
            try {
                val response = window.fetch(
                    "/api/shorten/$shortKey",
                    RequestInit(method = "DELETE")
                ).await()
                
                if (response.ok) {
                    message = "URL이 삭제되었습니다."
                    fetchUrls()
                } else {
                    message = "URL 삭제에 실패했습니다."
                }
            } catch (e: Exception) {
                console.error("Error deleting URL", e)
                message = "오류: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    useEffect {
        fetchUrls()
    }
    
    div {
        css {
            padding = 20.px
        }
        
        h1 {
            +"URL 단축기 관리자"
        }
        
        div {
            css {
                marginBottom = 20.px
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 10.px
            }
            
            div {
                css {
                    display = Display.flex
                    gap = 10.px
                }
                
                input {
                    css {
                        padding = 8.px
                        flexGrow = number(1.0)
                    }
                    type = InputType.text
                    placeholder = "단축할 URL을 입력하세요"
                    value = inputUrl
                    onChange = { event -> inputUrl = (event.target as HTMLInputElement).value }
                }
                
                button {
                    css {
                        padding = 8.px
                        backgroundColor = Color("#0066cc")
                        color = Color.white
                        border = None.none
                        borderRadius = 4.px
                        cursor = Cursor.pointer
                        hover {
                            backgroundColor = Color("#0055aa")
                        }
                    }
                    onClick = { shortenUrl() }
                    disabled = isLoading
                    +"단축하기"
                }
            }
            
            message?.let {
                p {
                    css {
                        color = Color("#333")
                        backgroundColor = Color("#f8f8f8")
                        padding = 10.px
                        borderRadius = 4.px
                    }
                    +it
                }
            }
        }
        
        div {
            css {
                marginTop = 20.px
            }
            
            if (isLoading) {
                p { +"로딩 중..." }
            } else if (urls.isEmpty()) {
                p { +"저장된 URL이 없습니다." }
            } else {
                table {
                    css {
                        width = 100.pct
                        borderCollapse = BorderCollapse.collapse
                        th {
                            backgroundColor = Color("#f2f2f2")
                            padding = 8.px
                            textAlign = TextAlign.left
                        }
                        td {
                            padding = 8.px
                            borderTop = Border(1.px, LineStyle.solid, Color("#ddd"))
                        }
                    }
                    
                    thead {
                        tr {
                            th { +"단축 키" }
                            th { +"원본 URL" }
                            th { +"생성 시간" }
                            th { +"작업" }
                        }
                    }
                    
                    tbody {
                        urls.forEach { url ->
                            tr {
                                td { +url.shortKey }
                                td { 
                                    css {
                                        maxWidth = 400.px
                                        overflow = Overflow.hidden
                                        textOverflow = TextOverflow.ellipsis
                                        whiteSpace = WhiteSpace.nowrap
                                    }
                                    +url.originalUrl 
                                }
                                td { +url.createdAt }
                                td {
                                    button {
                                        css {
                                            padding = 5.px
                                            backgroundColor = Color("#dc3545")
                                            color = Color.white
                                            border = None.none
                                            borderRadius = 4.px
                                            cursor = Cursor.pointer
                                            hover {
                                                backgroundColor = Color("#bd2130")
                                            }
                                        }
                                        onClick = { deleteUrl(url.shortKey) }
                                        +"삭제"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
