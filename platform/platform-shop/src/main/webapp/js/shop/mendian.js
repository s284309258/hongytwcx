$(function () {
    $("#jqGrid").Grid({
        url: '../mendian/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '名称', name: 'mendian_name', index: 'mendian_name', width: 80},
            {label: '地址', name: 'mendian_address', index: 'mendian_address', width: 80},
            {label: '电话', name: 'mendian_tel', index: 'mendian_tel', width: 80},
            {label: '营业时间', name: 'mendian_bustime', index: 'mendian_bustime', width: 80}
            ]
    });
});


var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        mendian: {isHot: 0, isDefault: 0, isShow: 1, type: 0},
        ruleValidate: {
            mendian_name: [
                {required: true, message: '门店名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        },
        status: ''
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
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
            var url = vm.mendian.id == null ? "../mendian/save" : "../mendian/update";
            debugger
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.mendian),
                successCallback: function () {
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
                    url: "../mendian/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });

            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../mendian/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.mendian = r.MendianEntity;
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
        // changeEnable: function () {
        //     if (vm.status) {
        //         vm.attributeCategory.enabled = 1;
        //     } else {
        //         vm.attributeCategory.enabled = 0;
        //     }
        // },
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