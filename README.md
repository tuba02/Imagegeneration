# Imagegeneration

## 概要
このプロジェクトはアプリで画像生成を行うアプリのコードとサーバ用のプログラムです。
アプリ側は画像生成のための文字列をサーバに送信し、サーバが生成した画像を受け取って表示します
サーバ側は文字列を受け取ったあと画像の生成を行いアプリ側に画像データを送信します

## 必要な環境
・ Python 3.10 以上
・ Flask
・ Waitress
・ Ngrok
・ diffusers
・ torch
・ transformers
・ accelerate
・ GPU を搭載している PC（NVIDIA GeForce RTX 4060 以上を推奨）

## インストール方法
1.Android Studioをインストール
  以下のページからインストーラーをダウンロードしてインストールしてください
　ダウンロード先:https://developer.android.com/studio
 インストール時にAVDをインストールしておくことで、仮想アンドロイド端末を使って、アプリケーションのテストが行えるようになります
 
2.リポジトリのクローン
  git clone https://github.com/tuba02/Imagegeneration.git
  
3.Android Studioのセットアップ
　3.1 C:\AndroidStudioProjects\Imagegeneration を開く。
　3.2 File メニューの Sync Project with Gradle Files をクリック。

4.SDKの設定
　Tools → SDK Managerを開き、以下のSDKをインストール
  ・ SDK Platforms
    ・ Android SDK Platform 36
    ・ Google APIs Intel x86_64 Atom System Image
  ・ SDK Platforms
    ・ Android SDK Build-Tools　36.00
    ・ Android Emulator
    ・ Android SDK Platform-Tools

5.仮想デバイスの作成
  5.1 Tools → Device Manager を開く。
  5.2 + ボタンから Create Virtual Device を選択。
  5.3 Pixel 8 Pro を選択 → Next。
  5.4 API 36 を選択 → Next。
  5.5 名前を入力 → Finish
  
6.アプリの起動
　緑色の Run app ボタンを押して仮想デバイスでアプリを実行。

## サーバーのセットアップ

7.サーバコードのダウンロード
  git clone --branch master https://github.com/tuba02/Imagegeneration.git

8.依存ライブラリのインストール
  pip install waitress flask
  pip install pyngrok

9.Ngrokの設定
  ngrok http 8000
  ターミナルに表示された Forwarding の https://youradress.ngrok-free.app をコピーし、
　app/java/com.example.imagegen/MainActivity の 310 行目の URL url = new URL("/api"); を編集して反映。
  Run appボタンで再度アプリを起動

10.サーバの起動
  waitress-serve --host=0.0.0.0 --port=8000 --threads=4 server:app


## 画像生成の詳細
本プロジェクトは Animagine XL 3.0 をベースにした画像生成モデルAnimagine XL 3.1を使用しています。
モデルは HuggingFaceから提供されており、Stable Diffusionベースのアーキテクチャを使用しています。

使用ライブラリ
・ diffusers
・ torch
・ transformers
・ acccelerate

## ライセンス
Animagine XL 3.1はFair AI Public License 1.0-SDのもとで提供されており、Stable Diffusionモデルのライセンスと互換性があります。
主なライセンス条件
1.変更の共有:Animagine XL 3.1 を改変した場合、その変更点と元のライセンスを共有する必要があります。
2.ソースコードの公開: ネットワーク経由でアクセス可能な場合、ソースコードを提供する必要があります。
3.配布条件: 本ライセンスまたは類似のライセンスの下でのみ配布可能。
4.コンプライアンス: ライセンス違反が発生した場合、30日以内に修正しないとライセンスが終了。

このライセンスの目的は、Animagine XL 3.1をオープンで改変可能な状態に保ち、オープンソースコミュニティの精神に沿った運用を促進することです。貢献者とユーザーを保護し、協力的で倫理的なオープンソースコミュニティを育むために設計されています。


注意：
本ライセンスには商業利用や再配布に関する条件も含まれているため、プロジェクトを利用・配布する際は必ずライセンスに従ってください。




  
