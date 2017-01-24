Zabbix 監視設定収集ツール
=========================

システム概要
------------

Zabbix 監視設定情報を収集します。
[Zabbix JSON-RPC API](https://www.zabbix.com/documentation/1.8/api/getting_started)
を用いて Zabbix の監視設定情報を収集します。
=======
VMWare仮想化インフラで構築したサーバに対して、システム構成情報を収集して、
収集した結果から各設定内容の検査を行います。

システム構成は以下の通りです。

![System configuration](image/system.png)

システム要件
------------

**検査対象サーバ**

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

Reference
---------

* [Zabbix JSON-RPC](https://www.zabbix.com/documentation/1.8/api/getting_started)
* [DavidWebb](http://hgoebl.github.io/DavidWebb/)

AUTHOR
-----------

Minoru Furusawa <minoru.furusawa@toshiba.co.jp>

COPYRIGHT
-----------

Copyright 2014-2016, Minoru Furusawa, Toshiba corporation.
