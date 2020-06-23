$(function () {
    $("#jqGrid").Grid({
        url: '../huiyuanyinhangka/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true,width: 30},
            {label: 'user_id', name: 'user_id', index: 'user_id', width: 80,hidden:true},
            {label: '会员昵称', name: 'nick_name', index: 'nick_name', width: 80},
            {label: '手机号', name: 'mobile', index: 'mobile', width: 80},
            {label: '银行户名', name: 'bank_account_name', index: 'bank_account_name', width: 80},
            {label: '银行账号', name: 'bank_account', index: 'bank_account', width: 80},
            {label: '开户行', name: 'bank_name', index: 'bank_name', width: 80},
            {label: '账户余额(可提现金额)', name: 'balance', index: 'balance', width: 80},
            {label: '累计提现金额', name: 'withdraw_money', index: 'withdraw_money', width: 80},
            {label: '总收入', name: 'total_money', index: 'total_money', width: 80}
        ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        huiyuanyinhangka: {

        },
        ruleValidate: {
            // name: [
            //     {required: true, message: '名字不能为空', trigger: 'blur'}
            // ],
            // description: [
            //     {required: true, message: '描述不能为空', trigger: 'blur'}
            // ]
        },
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
        saveOrUpdate: function (event) {
            var url = vm.huiyuanyinhangka.id == null ? "../huiyuanyinhangka/save" : "../huiyuanyinhangka/update";
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.huiyuanyinhangka),
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
                    url: "../huiyuanyinhangka/delete",
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
                url: "../huiyuanyinhangka/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.huiyuanyinhangka = r.huiyuanyinhangka.huiyuanyinhangka;
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