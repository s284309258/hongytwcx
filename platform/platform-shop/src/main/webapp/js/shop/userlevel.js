$(function () {
    $("#jqGrid").Grid({
        url: '../userlevel/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true},
            {label: '等级名称', name: 'name', index: 'name', width: 80},
            {label: '需要达到的任务量', name: 'taskNum', index: 'taskNum', width: 80},
            {label: '团队业绩提成百分比', name: 'teamReward', index: 'teamReward', width: 80},
            {label: '直推会员提成百分比', name: 'referenceReward', index: 'referenceReward', width: 80},
            {label: '会员升级所需金额', name: 'upgrade', index: 'upgrade', width: 80},
            {label: '描述', name: 'description', index: 'description', width: 80}
            ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        userLevel: {},
        ruleValidate: {
            name: [
                {required: true, message: '名字不能为空', trigger: 'blur'}
            ],
            description: [
                {required: true, message: '描述不能为空', trigger: 'blur'}
            ]
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
                postData: {'name': vm.q.name},
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