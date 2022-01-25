addEventListener("fetch", event => {
  let response = handleRequest(event.request)
  return event.respondWith(response)
})

async function handleRequest(request) {
  const { method, headers } = request

  if (method === "POST" && headers.get("content-type") === "application/json") {
    const json = await request.json()
    const toUrl = json.to
    console.log(toUrl)
    const data = json.data
    let response = await fetch(toUrl, {
      method: "POST",
      headers,
      body: JSON.stringify(data)
    })
    const results = await gatherResponse(response)
    return new Response(results, {
      headers: {
        "content-type": "application/json;charset=UTF-8",
      },
    })
  }
  return new Response("error", { status: 403 })
}

async function gatherResponse(response) {
  const { headers } = response
  const contentType = headers.get("content-type") || ""
  if (contentType.includes("application/json")) {
    return JSON.stringify(await response.json())
  } else {
    return response.text()
  }
}