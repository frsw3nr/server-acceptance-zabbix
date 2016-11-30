package InfraTestSpec

import groovy.util.logging.Slf4j
import groovy.transform.InheritConstructors
import org.apache.commons.io.FileUtils.*
import static groovy.json.JsonOutput.*
import org.hidetake.groovy.ssh.Ssh
import org.json.JSONObject
import org.json.JSONException
import groovy.json.*
import com.goebl.david.Webb
import jp.co.toshiba.ITInfra.acceptance.InfraTestSpec.*
import jp.co.toshiba.ITInfra.acceptance.*

@Slf4j
@InheritConstructors
class ZabbixSpec extends InfraTestSpec {

    static final def zabbix_labels = [
        'status' : [
            '0' : 'Monitored',
            '1' : 'Unmonitored',
        ],
        'available' : [
            '0' : 'Unknown',
            '1' : 'Available',
            '2' : 'Unavailable',
        ]
    ]
    String zabbix_ip
    String zabbix_user
    String zabbix_password
    String target_server
    String url
    String token
    int    timeout = 30
    def host_ids = [:]

    def init() {
        super.init()

        def remote_account = test_server.remote_account
        this.zabbix_ip       = remote_account['server']
        this.zabbix_user     = remote_account['user']
        this.zabbix_password = remote_account['password']
        this.target_server   = test_server.ip
        this.timeout         = test_server.timeout
    }

    def finish() {
        super.finish()
    }

    def setup_exec(TestItem[] test_items) {

        def json = JsonOutput.toJson(
            [
                jsonrpc: "2.0",
                method: "user.login",
                params: [
                    user:     this.zabbix_user,
                    password: this.zabbix_password,
                ],
                id: "1",
            ]
        )

        Webb webb = Webb.create()
        url = "http://${this.zabbix_ip}/zabbix/api_jsonrpc.php"
        JSONObject result = webb.post(url)
                                    .header("Content-Type", "application/json")
                                    .useCaches(false)
                                    .body(json)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
        token = result.getString("result")

        test_items.each {
            def method = this.metaClass.getMetaMethod(it.test_id, TestItem)
            if (method) {
                log.debug "Invoke command '${method.name}()'"
                try {
                    long start = System.currentTimeMillis();
                    method.invoke(this, it)
                    long elapsed = System.currentTimeMillis() - start
                    log.debug "Finish test method '${method.name}()' in ${this.server_name}, Elapsed : ${elapsed} ms"
                    it.succeed = 1
                } catch (Exception e) {
                    it.verify_status(false)
                    log.error "[Zabbix Test] Test method '${method.name}()' faild, skip.\n" + e
                }
            }
        }
    }

    def HostGroup(test_item) {
        if (target_server)
            return true

        def lines = exec('HostGroup') {

            def json = JsonOutput.toJson(
                [
                    jsonrpc: "2.0",
                    method: "HostGroup.get",
                    params: [
                        output: "extend",
                    ],
                    id: "1",
                    auth: token,
                ]
            )
            Webb webb = Webb.create();
            JSONObject result = webb.post(url)
                                        .header("Content-Type", "application/json")
                                        .useCaches(false)
                                        .body(json)
                                        .ensureSuccess()
                                        .asJsonObject()
                                        .getBody();

            def content = result.getString("result")
            new File("${local_dir}/HostGroup").text = content
            return content
        }

        def jsonSlurper = new JsonSlurper()
        def host_groups = jsonSlurper.parseText(lines)

        def headers = ['groupid', 'name']
        def csv = []
        def name
        host_groups.each { host_group ->
            def columns = []
            headers.each {
                columns.add(host_group[it] ?: 'NaN')
            }
            csv << columns
        }
        test_item.devices(csv, headers)
        test_item.results(csv.size().toString())
    }

    def User(test_item) {
        if (target_server)
            return true

        def lines = exec('User') {

            def json = JsonOutput.toJson(
                [
                    jsonrpc: "2.0",
                    method: "User.get",
                    params: [
                        output: "extend",
                        selectMedias: "extend",
                        selectUsrgrps: "extend",
                    ],
                    id: "1",
                    auth: token,
                ]
            )
            Webb webb = Webb.create();
            JSONObject result = webb.post(url)
                                        .header("Content-Type", "application/json")
                                        .useCaches(false)
                                        .body(json)
                                        .ensureSuccess()
                                        .asJsonObject()
                                        .getBody();

            def content = result.getString("result")
            new File("${local_dir}/User").text = content
            return content
        }

        def jsonSlurper = new JsonSlurper()
        def users = jsonSlurper.parseText(lines)

        def headers = ['userid', 'usrgrps', 'alias', 'name', 'medias']
        def csv = []
        users.each { user ->
            def columns = []
            headers.each {
                if (it == 'usrgrps') {
                    def usrgrps = []
                    user[it].each { usrgrp ->
                        usrgrps.add(usrgrp['name'])
                    }
                    columns.add(usrgrps.toString())

                } else if (it == 'medias') {
                    def medias = []
                    user[it].each { media ->
                        medias.add(media['sendto'])
                    }
                    columns.add(medias.toString())

                } else {
                    columns.add(user[it] ?: 'NaN')
                }
            }
            csv << columns
        }
        test_item.devices(csv, headers)
        test_item.results(csv.size().toString())
    }

    def Host(test_item) {
        def lines = exec('Host') {

            def params = [
                output: "extend",
                selectInterfaces: "extend",
                selectGroups: "extend",
                selectParentTemplates: "extend",
            ]
            if (target_server) {
                params['filter'] = [
                    'host' : target_server
                ]
            }

            def json = JsonOutput.toJson(
                [
                    jsonrpc: "2.0",
                    method: "Host.get",
                    params: params,
                    id: "1",
                    auth: token,
                ]
            )
            Webb webb = Webb.create();
            JSONObject result = webb.post(url)
                                        .header("Content-Type", "application/json")
                                        .useCaches(false)
                                        .body(json)
                                        .ensureSuccess()
                                        .asJsonObject()
                                        .getBody();

            def content = result.getString("result")
            new File("${local_dir}/Host").text = content
            return content
        }

        def jsonSlurper = new JsonSlurper()
        def hosts = jsonSlurper.parseText(lines)

        def headers = ['hostid', 'groups', 'parentTemplates', 'host', 'name',
                       'interfaces', 'status', 'available', 'error']
        def csv = []
        def host_info = [:]
        hosts.each { host ->
            def columns = []
            headers.each {
                def value = 'NaN'
                if (it == 'groups') {
                    def groups = []
                    host[it].each { group ->
                        groups.add(group['name'])
                    }
                    value = groups.toString()

                } else if (it == 'parentTemplates') {
                    def templates = []
                    host[it].each { template ->
                        templates.add(template['name'])
                    }
                    value = templates.toString()

                } else if (it == 'interfaces') {
                    def addresses = []
                    host[it].each { addr ->
                        addresses.add((addr['useip'] == '1') ? addr['ip'] : addr['dns'])
                    }
                    value = addresses.toString()

                } else if (it == 'status' || it == 'available') {
                    def id = host[it]
                    value = zabbix_labels[it][id]

                } else {
                    value = host[it]
                }
                if (target_server)
                    host_info[it] = value
                columns.add(value)

                if (it == 'hostid')
                    host_ids[target_server] = value
            }
            csv << columns
        }
        test_item.devices(csv, headers)
        host_info['Host'] = csv.size()
        test_item.results(host_info)
    }

    def linux_syslog(test_item) {
        if (target_server == null)
            return
        if(!host_ids.containsKey(target_server)) {
            log.error "Can't find host_id of ${target_server}, 'linux_syslog' test needs 'Host' test before."
        }

        def lines = exec('linux_syslog') {

            def params = [
                output: "extend",
                hostids: [host_ids[target_server]],
                search: [
                    name: "SystemLog",
                ],
            ]
            def json = JsonOutput.toJson(
                [
                    jsonrpc: "2.0",
                    method: "Item.get",
                    params: params,
                    id: "1",
                    auth: token,
                ]
            )
            Webb webb = Webb.create();
            JSONObject result = webb.post(url)
                                        .header("Content-Type", "application/json")
                                        .useCaches(false)
                                        .body(json)
                                        .ensureSuccess()
                                        .asJsonObject()
                                        .getBody();

            def content = result.getString("result")
            new File("${local_dir}/linux_syslog").text = content
            return content
        }

        def jsonSlurper = new JsonSlurper()
        def results = jsonSlurper.parseText(lines)

        def lastlogsize = '0'
        if (results.size() == 1) {
            lastlogsize = results[0]['lastlogsize']
        }
        test_item.results(lastlogsize)

    }
}
