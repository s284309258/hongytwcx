$(function () {
    $("#jqGrid").Grid({
        url: '../huiyuanmingxibaobiao/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '董事', name: 'dongshi', index: 'dongshi', width: 80},
            {label: '合伙人', name: 'hehuoren', index: 'hehuoren', width: 80},
            {label: '代理商', name: 'dailishang', index: 'dailishang', width: 80},
            {label: '直推人', name: 'zhituiren', index: 'zhituiren', width: 80},
            {label: '会员姓名', name: 'nickname', index: 'nickname', width: 80},
            {label: '会员手机号', name: 'mobile', index: 'mobile', width: 80},
            {label: '注册日期', name: 'register_time', index: 'register_time', width: 80},
            {label: '银行账户户名', name: 'bank_account_name', index: 'bank_account_name', width: 80},
            {label: '银行账号', name: 'bank_account', index: 'bank_account', width: 80},
            {label: '开户行', name: 'bank_name', index: 'bank_name', width: 80},
        ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        huiyuanmingxibaobiao: {},
        // ruleValidate: {
        //     name: [
        //         {required: true, message: '名字不能为空', trigger: 'blur'}
        //     ],
        //     description: [
        //         {required: true, message: '描述不能为空', trigger: 'blur'}
        //     ]
        // },
        q: {
            name: ''
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.userLevel = {};
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        exportData: function(event){
            exportFile('#rrapp', '../huiyuanmingxibaobiao/export', {'keyword': vm.q.keyword});
        },
        saveOrUpdate: function (event) {
            var url = vm.userLevel.id == null ? "../userlevel/save" : "../userlevel/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.userLevel),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../userlevel/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../userlevel/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.userLevel = r.userLevel;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'keyword': vm.q.keyword},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
    }
});