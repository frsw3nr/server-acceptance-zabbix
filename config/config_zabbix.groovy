// �����d�l�V�[�g��`

evidence.source = './�Ď��ݒ�`�F�b�N�V�[�g_Zabbix.xlsx'
evidence.sheet_name_server = '�`�F�b�N�Ώ�'
evidence.sheet_name_rule = '�������[��'
evidence.sheet_name_spec = [
    'Zabbix':   '�Ď��ݒ�`�F�b�N�V�[�g(Zabbix)',
]

// �������ʃt�@�C���o�͐�

evidence.target='./build/�Ď��ݒ�`�F�b�N�V�[�g_Zabbix_<date>.xlsx'

// �������ʃ��O�f�B���N�g��

evidence.staging_dir='./build/log'

// ���񉻂��Ȃ��^�X�N
// ����x���w������Ă��A�w�肵���h���C���^�X�N�̓V���A���Ɏ��s����

test.serialization.tasks = []

// DryRun���[�h���O�ۑ���

test.dry_run_staging_dir = './src/test/resources/log/'

// �R�}���h�̎�̃^�C���A�E�g
test.Zabbix.timeout = 300

// Zabbix�ڑ����

account.Remote.Test.server   = 'panda1'
account.Remote.Test.user     = 'admin'
account.Remote.Test.password = 'getperf'

account.Remote.Test2.server   = 'yps4za'
account.Remote.Test2.user     = 'Admin'
account.Remote.Test2.password = 'zabbix'

// Redmine �t�B���^�[�ݒ�

redmine.fixed_items    = [
	'platform' : 'Zabbix'
]
redmine.default_values = [
	'remote_account_id' : 'Test',
	'verify_id' : 'RuleAP',
]
redmine.required_items = ['server_name', 'platform', 'remote_account_id']

redmine.default_filter_options = [
    'project': '�\���Ǘ����؃T�C�g',
    'status': '%',
    'version': '%',
    'tracker': 'IA�T�[�o�[',
]


