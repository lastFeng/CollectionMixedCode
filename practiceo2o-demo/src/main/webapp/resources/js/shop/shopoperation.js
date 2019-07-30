/**
 * 获取列表信息
 *  商铺分类
 *  区域
 * */
$(function () {
    var initUrl = '/shopadmin/getshopinitinfo';
    var registerShopUrl = '/shopadmin/registershop';
    // 调试信息
    // alert(initUrl);
    getShopInitInfo();

    // 获取店铺基本信息
    function getShopInitInfo() {

        // 新建内容
        $.getJson(initUrl, function (data) {
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });

                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + ittem.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-catetory').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });

        // 提交按钮
        $('#submit').click(function () {
            var shop = {};
            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();
            shop.shopCategory = {
                shopCategoryId: $('#shop-category').find('option').not(function () {
                    return !this.selected;
                }).data('id')
            };
            shop.area = {
                areaId: $('#shop-area').find('option').not(function () {
                    return !this.selected;
                })
            };

            var shopImg = $('#shop-img')[0].files[0];
            var formData = new FormData();
            formData.append('shopImg', shopImg);
            formData.append('shopStr', JSON.stringify(shop));
            $.ajax({
                url: registerShopUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast("提交成功");
                    } else {
                        $.toast("提交失败！" + data.errMsg);
                    }
                }
            })
        })
    }
})