# mcp-demo

> Model Context Protocol (MCP)
> は、LLMアプリケーションと外部データソースやツールをシームレスに統合するためのオープンプロトコルです。  
> AIベースのIDE、チャットインターフェース、カスタムAIワークフローなどにおいて、LLMが必要とするコンテキストに接続するための標準化された方法を提供します。

[한국어 README](https://github.com/sionic-ai/mcp-demo/tree/ko)  
[English README](https://github.com/sionic-ai/mcp-demo)  
[日本語 README](https://github.com/sionic-ai/mcp-demo/tree/ja)

### ステップ 0: MCP 連携

[mcp-jetbrains github](https://github.com/JetBrains/mcp-jetbrains#)

> IDEでプロジェクトをチェックアウトし、プロジェクトでサポートされているすべてのAPIを確認してください

### ステップ 1: 基本的なURL短縮サービスの実装

- `urlShortener` サービスを作成します。
- `POST /api/shorten` を実装します。
  - 英数字のみで構成され、文字から始まる6文字のランダムなキーを生成します。
  - インメモリのハッシュマップに保存し、HTTP 200 OK とともに短縮URLを返します。
- `GET /api/shorten/{shortKey}` を実装します。
  - 指定されたショートキーに対応する元のURLを返却します。
- Controller、Service、Repositoryレイヤーに分けて構造化してください。
- 利便性機能の追加：
  - 手動でテスト可能な HTTP クライアント用ファイル を作成します。
  - 主要な処理フローに infoレベルのログ を追加します。
  - ロギングには、`io.github.oshai:kotlin-logging-jvm` 依存関係を使用してください。

### ステップ 2: envごとにデータストアを分離

- `local env` では、現在のロジック（インメモリのハッシュマップ）をそのまま使用します。
- `local_postgres env` では、Exposed を使用したいと考えています。依存関係を追加してください。
- `local_postgres env` で使用するDBは、すでに docker-compose により起動済みです。
- 接続情報は、`docker/docker-compose.yml` に記載されています。
- `src/main/resources/application.yml` にenvを追加してください。
- `local env` ：インメモリのハッシュマップを引き続き使用
- `local_postgres env` ：Exposed を使って PostgreSQL に接続
  - 接続情報は `docker/docker-compose.yml` の PostgreSQL 設定を利用

### ステップ 3: Kotlin Multiplatformによる管理画面の実装

- 実装機能：
  - URL入力フォーム：長いURLを入力するフィールドと「短縮する」ボタンを配置
  - URL一覧表示：短縮キー、元のURL、作成日時を表示
  - 削除機能：各URL項目に削除ボタンを追加
- 追加APIエンドポイント：
  - `GET /api/shorten/urls`：すべての短縮URL一覧を取得
  - `DELETE /api/shorten/{shortKey}`：指定した短縮URLを削除
- 動作フロー：
  - `POST` や `DELETE` 操作完了後に、自動でURL一覧を再読み込み
  - ページ読み込み時に、初期のURL一覧を取得して表示
