---
description: コード品質チェック & テスト実行
---

# コード品質チェック & テスト実行

このワークフローは、コードのフォーマットチェック、静的解析、テスト実行、カバレッジレポート生成を行います。

```yaml
1. コードのフォーマットを適用:
   mvn com.spotify.fmt:fmt-maven-plugin:format

2. コードの静的解析を実行:
   mvn checkstyle:check pmd:check

3. ユニットテストを実行し、カバレッジレポートを生成:
   mvn test jacoco:report

4. カバレッジレポートを確認:
   - ブラウザで `target/site/jacoco/index.html` を開いてください

5. PMDレポートを確認:
   - ブラウザで `target/site/pmd.html` を開いてください
```

## 使用方法

プロジェクトのルートディレクトリで以下を実行:
```
/code-quality-check
```

## 注意点
- フォーマットは自動的に適用されます
- チェックスタイルやPMDのエラーがある場合はビルドが失敗します
- テストが失敗した場合は、ワークフローはそこで停止します
- カバレッジレポートは `target/site/jacoco/index.html` で確認可能です