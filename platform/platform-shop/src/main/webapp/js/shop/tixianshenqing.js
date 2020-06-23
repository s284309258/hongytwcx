$(function () {
    $("#jqGrid").Grid({
        url: '../tixianshenqing/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true,width: 30},
            {label: 'user_id', name: 'user_id', index: 'user_id', width: 80,hidden:true},
            {label: '会员昵称', name: 'nick_name', index: 'nick_name', width: 80},
            {label: '手机号', name: 'mobile', index: 'mobile', width: 80},
            {label: '申请提现的金额', name: 'apply_withdraw_money', index: 'apply_withdraw_money', width: 80},
            {label: '申请提现日期', name: 'apply_date', index: 'apply_date', width: 80},
            {label: '银行户名', name: 'bank_account_name', index: 'bank_account_name', width: 80},
            {label: '银行账号', name: 'bank_account', index: 'bank_account', width: 90},
            {label: '开户行', name: 'bank_name', index: 'bank_name', width: 100},
            {label: '提现状态(成功,失败,申请中)', name: 'state', index: 'state', width: 100},
            {label: '备注', name: 'remark', index: 'remark', width: 80}
        ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        tixianshenqing: {

        },
        // ruleValidate: {
        //     name: [
        //         {required: true, message: '名字不能为空', trigger: 'blur'}
        //     ],
        //     description: [
        //         {required: true, message: '描述不能为空', trigger: 'blur'}
        //     ]
        // },
        q: {
            keyword: ''
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
            exportFile('#rrapp', '../tixianshenqing/export', {'keyword': vm.q.keyword});
        },
        successPay: function(event){
            var ids = getSelectedRows("#jqGrid");
            Ajax.request({
                type: "POST",
                url: "../tixianshenqing/successPay",
                contentType: "application/json",
                params: JSON.stringify({ids:ids,success:'success'}),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        failPay: function(event){
            var ids = getSelectedRows("#jqGrid");
            Ajax.request({
                type: "POST",
                url: "../tixianshenqing/successPay",
                contentType: "application/json",
                params: JSON.stringify({ids:ids,success:'fail'}),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.userLevel.id == null ? "../tixianshenqing/save" : "../huiyuanyinhangka/update";
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.tixianshenqing),
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
                    url: "../tixianshenqing/delete",
                    contentType: "application/json",
                    params: JSON.stringify({ids:ids,success:'success'}),
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
                url: "../tixianshenqing/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.tixianshenqing = r.tixianshenqing.tixianshenqing;
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