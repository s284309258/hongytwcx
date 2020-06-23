$(function () {
    $("#jqGrid").Grid({
        url: '../xiaofeimingxibaobiao/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '董事', name: 'dongshi_nickname', index: 'dongshi_nickname', width: 80},
            {label: '合伙人', name: 'hehuoren_nickname', index: 'hehuoren_nickname', width: 80},
            {label: '代理商', name: 'daili_nickname', index: 'daili_nickname', width: 80},
            {label: '直推人', name: 'huiyuanzhishu_nickname', index: 'huiyuanzhishu_nickname', width: 80},
            {label: '会员姓名', name: 'huiyuan_nickname', index: 'huiyuan_nickname', width: 80},
            {label: '会员手机号', name: 'mobile', index: 'mobile', width: 80},
            {label: '产品名称', name: 'goods_name', index: 'goods_name', width: 80},
            {label: '产品价格', name: 'goods_price', index: 'goods_price', width: 80},
            {label: '产品数量', name: 'goods_num', index: 'goods_num', width: 80},
            {label: '支付总价格', name: 'goods_total_pay', index: 'goods_total_pay', width: 80},
            {label: '收货人姓名', name: 'shouhuo_name', index: 'shouhuo_name', width: 80},
            {label: '收货人手机号', name: 'shouhuo_tel', index: 'shouhuo_tel', width: 80},
            {label: '收货人地址', name: 'shouhuo_address', index: 'shouhuo_address', width: 80},
            {label: '购买日期', name: 'consumer_time', index: 'consumer_time', width: 80}
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
            exportFile('#rrapp', '../xiaofeimingxibaobiao/export', {'keyword': vm.q.keyword});
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