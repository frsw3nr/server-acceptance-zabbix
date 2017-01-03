// 検査仕様シート定義

evidence.source = './監視設定チェックシート_Zabbix.xlsx'
evidence.sheet_name_server = 'チェック対象'
evidence.sheet_name_rule = '検査ルール'
evidence.sheet_name_spec = [
    'Zabbix':   '監視設定チェックシート(Zabbix)',
]

// 検査結果ファイル出力先

evidence.target='./build/監視設定チェックシート_Zabbix_<date>.xlsx'

// 検査結果ログディレクトリ

evidence.staging_dir='./build/log'

// 並列化しないタスク
// 並列度を指定をしても、指定したドメインタスクはシリアルに実行する

test.serialization.tasks = []

// DryRunモードログ保存先

test.dry_run_staging_dir = './src/test/resources/log/'

// コマンド採取のタイムアウト
test.Zabbix.timeout = 300

// Zabbix接続情報

account.Remote.Test.server   = 'ostrich'
account.Remote.Test.user     = 'admin'
account.Remote.Test.password = 'getperf'

account.Remote.Test2.server   = 'yps4za'
account.Remote.Test2.user     = 'Admin'
account.Remote.Test2.password = 'zabbix'
