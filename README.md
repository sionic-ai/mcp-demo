# mcp-demo

> Model Context Protocol (MCP) is an open protocol that enables seamless integration between LLM applications and
> external data sources and tools.  
> It provides a standardized method for connecting LLMs with necessary context in AI-based IDEs, chat interfaces, custom
> AI workflows, and more.

### Step 0: MCP Integration

[mcp-jetbrains github](https://github.com/JetBrains/mcp-jetbrains#)

> Check out my project in the IDE and give me all the supported APIs of the project

### Step 1: Implement Basic URL Shortening Service

- Create a urlShortener service
- Implement POST /api/shorten
    - Generate a random 6-character string key consisting of only letters and numbers, starting with a letter
    - Store in an in-memory hashmap and return the shortened URL with 200 OK
- Implement GET /api/shorten/{shortKey}
    - When the endpoint receives a short key, return the corresponding original URL
- Structure with Controller, Service, and Repository layers
- Add convenience features:
    - Create an http-client file for manual testing
    - Add info logs to main flows
    - Use io.github.oshai:kotlin-logging-jvm dependency for logging

### Step 2: Separate Data Storage by Environment

- Maintain current logic for local env
- Use Exposed for local_postgres env (add dependency)
- The database for local_postgres env is already running via docker-compose
- Connection information is available in docker/docker-compose.yml
- Add environments in src/main/resources/application.yml
- local env: Continue using in-memory hashmap
- local_postgres env: Connect to PostgreSQL DB using Exposed
    - Utilize PostgreSQL connection info from docker/docker-compose.yml

### Step 3: Implement Kotlin Multiplatform Admin Interface

- Features:
    - URL input form: Input field for long URLs and a 'Shorten' button
    - URL list display: Show short key, original URL, and creation time
    - Delete functionality: Add delete button to each URL entry
- Additional API endpoints:
    - GET /api/shorten/urls: Retrieve all shortened URL listings
    - DELETE /api/shorten/{shortKey}: Delete a specific shortened URL
- Workflow:
    - Automatically refresh URL list after POST and DELETE operations
    - Load initial URL list on page load
