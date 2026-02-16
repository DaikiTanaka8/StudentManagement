# StudentManagement（受講生管理システム）

## 概要
受講生・コース・申し込み情報を管理するバックエンドAPIアプリケーションです。  
SpringBootを用いて開発し、AWS上にデプロイしています。  

---

## 主な機能

- 受講生の新規登録
- 受講生情報の更新
- 受講生一覧取得
- 受講生の単一検索
- 条件指定による絞り込み検索
- コース・申し込み情報との紐づけ管理

---

## 使用技術

### バックエンド
- Java  
- Spring Boot  
- MyBatis  
- Lombok  

### インフラ
- AWS（EC2 / RDS / ALB）  
- MySQL  

### CI/CD
- GitHub Actions  
- systemd によるサービス管理  

### 開発ツール
- Postman  
- Swagger  
- IntelliJ IDEA  
- GitHub  

---

## API一覧

### 1. 受講生新規登録
**POST** `/registerStudent`

### 2. 受講生更新  
**PUT** `/updateStudent`

### 3. 受講生一覧取得  
**GET** `/studentList`

### 4. 受講生単一検索  
**GET** `/student/{studentId}`

### 5. 絞り込み検索  
**GET** `/students?fullName=&age=&gender=`

---

## 利用方法

本アプリはバックエンドAPIのみを実装しており、フロントエンドは含まれていません。  
APIの動作確認にはPostmanなどのAPIクライアントを使用してください。  

---

## CI/CD

GitHub Actions により以下を自動化しています。

- ビルド  
- テスト  
- EC2 へのデプロイ  

---

## 今後の改善予定

- フロントエンドの実装  
- 検索機能の拡張  
- セキュリティ強化  
- UIの追加  

---

## 注意事項

本アプリは学習目的で作成したものです。  
利用によって発生した問題について責任は負いかねます。  

---

## 作者

RaiseTech Javaコースにて開発
