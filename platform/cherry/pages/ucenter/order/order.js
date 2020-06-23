var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();
Page({
  data:{
    orderList: [],
    page: 1,
    size: 10,
    loadmoreText: '正在加载更多数据',
    nomoreText: '亲~没有更多了',
    nomore: false,
    totalPages: 1,
    currentId: '1',
    section: [{
      name: '自提订单',
      typeId: '1' 
    }, {
        name: '快递订单',
      typeId: '2'
    }],
    currentId2: '1',
    section2: [{
      name: '全部',
      typeId2: '1'
    }, {
        name: '待付款',
      typeId2: '2'
    },
      {
        name: '待收货',
        typeId2: '3'
      },
      {
        name: '已完成',
        typeId2: '4'
      },
      {
        name: '已取消',
        typeId2: '5'
      },
    ],
    showModal: false,

  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    // 页面显示
    wx.showLoading({
      title: '加载中...',
      success: function () {
      }
    });
    this.getOrderList();
  },

  /**
       * 页面上拉触底事件的处理函数
       */
  onReachBottom: function () {
    this.getOrderList()
  },
  getOrderList(){
  
    let that = this;
    if (that.data.totalPages <= that.data.page - 1) {
      that.setData({
        nomore: true
      })
      return;
    }
    util.request(api.OrderList, {page: that.data.page, size: that.data.size}).then(function (res) {
      console.log(456)
      if (res.errno === 0) {
      console.log(res.data.data)
        that.setData({
          orderList: res.data.data,
          page: res.data.currentPage + 1,
          totalPages: res.data.totalPages
        });
        var cannel_num = 0;
         var cannel_num2=0;
         var pending_num=0;
        var pending_num2 = 0;
        var reach=0;
        var reach2=0;
        var payment=0;
        var payment2 = 0;
        var orderl=0;
        var orderl2=0
        // console.log(res.data.data.count())
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 1){
            orderl++;
          }
        }
      for (var i = 0; i < res.data.data.length; i++) {
        if (res.data.data[i].take_way == 2) {
         
            orderl2++;
     
        }
      }
          for (var i = 0; i < res.data.data.length; i++) {
            if (res.data.data[i].take_way == 1){
              if (res.data.data[i].order_status == 0) {
                payment++;
              }
            }
          }
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 2) {
            if (res.data.data[i].order_status == 0) {
              payment2++;
            }
          }
        }
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 1) {
            if (res.data.data[i].order_status == 300) {
              pending_num++;
            }
          }
        }

        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 2) {
            if (res.data.data[i].order_status == 300) {
              pending_num2++;
            }
          }
        }
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 1) {
            if (res.data.data[i].order_status == 301) {
              reach++;
            }
          }
        }

        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 2) {
            if (res.data.data[i].order_status == 301) {
              reach2++;
            }
          }
        }
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 1) {
            if (res.data.data[i].order_status == 101) {
              cannel_num++;
            }
          }
        }

        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].take_way == 2) {
            if (res.data.data[i].order_status == 101) {
              cannel_num2++;
            }
          }
        }
        
        that.setData({
          cannel_num: cannel_num,
          cannel_num2: cannel_num2,
          pending_num: pending_num,
          pending_num2: pending_num2,
          reach: reach,
          reach2: reach2,
          payment: payment,
          payment2: payment2,
          orderl:orderl,
          orderl2:orderl2
        })
   
        wx.hideLoading();
    
        // console.log(that.data.orderList.concat(res.data.data))
      }
    
    });
  },
  payOrder(event){
      let that = this;
      let orderIndex = event.currentTarget.dataset.orderIndex;
      let order = that.data.orderList[orderIndex];
      util.request(api.PayPrepayId, {
        orderId: that.data.orderId || 15
      }).then(function (res) {
        console.log(res)
        if (res.errno === 0) {
          const payParam = res.data;
          wx.requestPayment({
            'timeStamp': payParam.timeStamp,
            'nonceStr': payParam.nonceStr,
            'package': payParam.package,
            'signType': payParam.signType,
            'paySign': payParam.paySign,
            'success': function (res) {
              console.log(res);
            },
            'fail': function (res) {
              console.log(res);
            }
          });
        }
      });
      // wx.redirectTo({
      //     url: '/pages/pay/pay?orderId=' + order.id + '&actualPrice=' + order.actual_price,
      // })
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    this.getOrderList()
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  },
  //点击每个导航的点击事件
  handleTap: function (e) {
    let id = e.currentTarget.id;
    if (id) {
      this.setData({
        currentId: id
      })
    }
  },
  handleTap2: function (e) {
    let id = e.currentTarget.id;
    if (id) {
      this.setData({
        currentId2: id
      })
    }
    // this.getOrderList()
  },
  /**
    * 弹窗
    */
  showDialogBtn: function () {
    this.setData({
      showModal: true
    })
  },
  /**
   * 弹出框蒙层截断touchmove事件
   */
  preventTouchMove: function () {
  },
  /**
   * 隐藏模态对话框
   */
  hideModal: function () {
    this.setData({
      showModal: false
    });
  },
  /**
   * 对话框取消按钮点击事件
   */
  onCancel: function () {
    this.hideModal();
  },
  /**
   * 对话框确认按钮点击事件
   */
  onConfirm: function () {
    this.hideModal();
  }

})