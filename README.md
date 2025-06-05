# WindSurf AI デモアプリケーション

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## プロジェクト概要

Windsurf AI デモアプリケーションは、Spring Boot 3.2.0 を使用したタスク管理のデモアプリケーションです。
RESTful APIとシンプルなWebインターフェースを提供し、タスクの作成・閲覧・更新・削除（CRUD）機能を実装しています。

## 主な機能

- タスク管理機能（CRUD操作）
- RESTful API エンドポイント
- シンプルなWebインターフェース
- H2 インメモリデータベース統合
- コード品質チェック（Checkstyle, PMD, JaCoCo）
- 自動コードフォーマット（Google Java Format）

## 技術スタック

- **バックエンド**: Spring Boot 3.2.0
- **フロントエンド**: Thymeleaf, HTML5, CSS3, JavaScript
- **データベース**: H2 Database (インメモリ)
- **ビルドツール**: Maven 3.6+
- **開発ツール**: Lombok, Spring Boot DevTools

## 必要条件

- Java 17 以降
- Maven 3.6 以降
- Git（オプション）

## セットアップ手順

### 1. リポジトリのクローン

```bash
git clone https://github.com/your-username/windsurfai-demo.git
cd windsurfai-demo
```

### 2. 依存関係のインストールとビルド

```bash
mvn clean install
```

### 3. アプリケーションの起動

通常起動:
```bash
mvn spring-boot:run
```

コードチェックをスキップして起動（開発時）:
```bash
mvn spring-boot:run "-Dcheckstyle.skip=true" "-Dpmd.skip=true"
```

アプリケーションはデフォルトで `http://localhost:8080` で起動します。

## API リファレンス

### タスク一覧取得
```
GET /api/tasks
```

### タスク作成
```
POST /api/tasks
Content-Type: application/json

{
    "title": "新しいタスク",
    "description": "タスクの詳細説明",
    "completed": false
}
```

### タスク詳細取得
```
GET /api/tasks/{id}
```

### タスク更新
```
PUT /api/tasks/{id}
Content-Type: application/json

{
    "title": "更新されたタスク",
    "description": "更新された説明",
    "completed": true
}
```

### タスク削除
```
DELETE /api/tasks/{id}
```

## 開発者向け情報

### コード品質チェック

```bash
# コードフォーマットの適用
mvn fmt:format

# 静的解析の実行
mvn checkstyle:check pmd:check

# テストの実行とカバレッジレポートの生成
mvn test jacoco:report
```

### レポートの確認

- JaCoCo カバレッジレポート: `target/site/jacoco/index.html`
- PMD レポート: `target/site/pmd.html`
- Checkstyle レポート: `target/checkstyle-result.xml`

### 開発用データベース

アプリケーション起動時にH2コンソールが利用可能です:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- ユーザー名: `sa`
- パスワード: (空欄)

## ビルドとデプロイ

スタンドアロンJARの作成:

```bash
mvn clean package
```

生成されたJARファイルは `target/windsurfai-demo-0.0.1-SNAPSHOT.jar` に出力されます。

## ライセンス

このプロジェクトは [MIT ライセンス](LICENSE) の下で公開されています。

## コントリビューション

プルリクエストやイシューは常に歓迎します。

1. リポジトリをフォークしてください
2. 機能ブランチを作成してください (`git checkout -b feature/amazing-feature`)
3. 変更をコミットしてください (`git commit -m 'Add some amazing feature'`)
4. ブランチにプッシュしてください (`git push origin feature/amazing-feature`)
5. プルリクエストを開いてください

## 著者

- Windsurf AI Team <contact@windsurfai.example.com>
