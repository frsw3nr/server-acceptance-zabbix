<<<<<<< HEAD
Zabbix 監視設定収集ツール
=========================
=======
サーバ構築エビデンス収集ツール
==============================
>>>>>>> d8c0ff5ed07bd8ec140b739ae74a643c7d21d969

システム概要
------------

<<<<<<< HEAD
Zabbix 監視設定情報を収集します。
[Zabbix JSON-RPC API](https://www.zabbix.com/documentation/1.8/api/getting_started)
を用いて Zabbix の監視設定情報を収集します。
=======
VMWare仮想化インフラで構築したサーバに対して、システム構成情報を収集して、
収集した結果から各設定内容の検査を行います。

システム構成は以下の通りです。

![System configuration](image/system.png)
>>>>>>> d8c0ff5ed07bd8ec140b739ae74a643c7d21d969

システム要件
------------

**検査対象サーバ**

<<<<<<< HEAD
* Zabbix サーバで JSON-RPC が利用できる環境が必要です。

**検査用PC**

* server-acceptance 本体が利用できる環境が必要です。
* 以下の通り、**システム環境変数 にserver-acceptanceのホームディレクトリのパス**を設定します。
    * スタート→コンピューター→コンピューターを右クリック→プロパティをクリックします。
    * システムの詳細設定を順にたどり、システムのプロパティを表示します。
    * 環境変数ボタンをクリックします。
    * システム環境変数のリストボックスの中からPathの行を選択して、編集ボタンをクリックします。
    * ;(セミコロン)で区切って行の最後にserver-acceptance のパスを追加します。
    * 設定が終わったら、OKボタンをクリックします。

利用方法
--------

1. 7-zip を用いて、 server-acceptance-zabbix.zip を解凍します。
2. 「監視設定チェックシート_Zabbix.xlsx」を開き、シート「チェック対象」に検査対象サーバの接続情報を記入します。
3. config/config.groovy 内のサーバアカウント情報を編集します。

        // Zabbix接続情報
        account.Remote.Test.server   = 'ostrich'
        account.Remote.Test.user     = 'admin'
        account.Remote.Test.password = 'getperf'

4. 解凍したディレクトリに移動し、getconfig コマンドを実行します。使用方法は以下の通りです。

        getconfig

5. 全ホストを対象に検査をする場合は、「監視設定チェックシート_ZabbixAll.xlsx」を編集して以下を実行してください。

        getconfig -e 監視設定チェックシート_ZabbixAll.xlsx
=======
* vCenter, Linux は 検査用PCから検査用アカウントでssh接続できる環境が必要。
* Windows は WMF4.0以上(PowerShell用)が必要です。Windows 2012 Server R2は標準インストールされています。Windows 2012 Server, Windows 2008 ServerはWMF 4.0のインストールが必要です。
* PowerShell のリモートアクセス許可設定が必要です。

WFM、PowerShell環境設定についての詳細は、**ドキュメント:使用方法** の事前準備を参照してください。

**検査用PC**

* JDK1.8以上
* WFM4.0以上(Windows検査用)
* PowerCLI 5.5以上(vCenter検査用)
* 7-zip(zip utility)、UTF-8が扱えるエディタ(Sakura Editor等)
* Excel 2007以上

ビルド方法
----------

GitHub サイトからリポジトリの zip ファイルをダウンロード・解凍して、以下のGradleタスクを実行します。

**Note** 英語版が必要な場合は、build.grade ファイル内の行を、
 def language  = 'en' に変更してください。

```
cd gradle-server-acceptance
gradlew test
gradlew zipApp
```

実行すると、以下アーカイブファイルが生成されます。

```
dir /b build\distributions
gradle-server-acceptance-0.1.1.zip
```

利用方法
--------

1. 7-zip を用いて、 gradle-server-acceptance-0.1.1.zip を解凍します。
2. 「チェックシート.xlsx」を開き、シート「チェック対象VM」に検査対象サーバの接続情報を記入します。
3. config/config.groovy 内のサーバアカウント情報を編集します。
4. server-acceptance ディレクトリに移動し、getconfig コマンドを実行します。使用方法は以下の通りです。

```
./getconfig -h
usage: getspec
 -c,--config <arg>     Config file path : ./config/config.groovy
 -d,--dry-run          Enable Dry run test
 -e,--excel <arg>      Excel test spec file path : check_sheet.xlsx
 -h,--help             Print usage
 -p,--parallel <arg>   Degree of test runner processes
 -r,--resource <arg>   Dry run test resource : ./src/test/resources/log/
 -s,--server <arg>     Filtering list of servers : svr1,svr2,...
 -t,--test <arg>       Filtering list of test_ids : vm,cpu,...
 -v,--verify           Disable verify test
```

検査項目のカスタマイズ
----------------------

以下の検査スクリプトを編集します。

```
dir /b lib\InfraTestSpec
LinuxSpec.groovy
vCenterSpec.groovy
WindowsSpec.groovy
```

検査IDと同一名のメソッドで検査コードを記述します。
既存の検査項目を変更する場合はコメントアウトを外してください。
詳細は、**ドキュメント:開発ガイド** を参照してください。
>>>>>>> d8c0ff5ed07bd8ec140b739ae74a643c7d21d969

Reference
---------

<<<<<<< HEAD
* [Zabbix JSON-RPC](https://www.zabbix.com/documentation/1.8/api/getting_started)
* [DavidWebb](http://hgoebl.github.io/DavidWebb/)
=======
* [Groovy SSH](https://github.com/int128/groovy-ssh)
* [Apache POI](https://poi.apache.org/)
* [PowerShell](https://github.com/PowerShell/PowerShell)
* [PowerCLI](https://www.vmware.com/support/developer/PowerCLI/)
>>>>>>> d8c0ff5ed07bd8ec140b739ae74a643c7d21d969

AUTHOR
-----------

Minoru Furusawa <minoru.furusawa@toshiba.co.jp>

COPYRIGHT
-----------

Copyright 2014-2016, Minoru Furusawa, Toshiba corporation.
