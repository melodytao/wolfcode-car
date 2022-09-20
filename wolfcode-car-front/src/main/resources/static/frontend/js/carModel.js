var carData = JSON.parse(sessionStorage.getItem('carData'))
var carList = []
var carMenuItem2 = ''
for (var i = 0; i < carData.length; i++) {
    var item = carData[i]
    carMenuItem2 += ' <li index="' + item.carName + '" class="left-menu-item"><i></i>' + item.carName + '</li>'
}
$('#carMenu2').html(carMenuItem2);
$(function () {
    $('.header').load('header.html');
    $('.footer').load('footer.html');
    // 选中菜单

    let type = getQueryVariable('id')
    $(`#carMenu2 li[index=${type}]`).addClass('left-menu-item-active')
    // 请求数据
    getData(type);
    // 切换车型
    $('#carMenu2').on('click', '.left-menu-item', function () {
        if (!$(this).hasClass('left-menu-item-active')) {
            $('.left-menu-item-active').removeClass('left-menu-item-active');
            $(this).addClass('left-menu-item-active');
            // 请求数据
            getData($(this).attr('index'));
        }
    });


    /**
     * 请求数据
     * @param type 车的类型
     */
    function getData(type) {
        $.ajax({
            type: "GET",
            url: baseUrl + "/system/type/typeName/" + type,
            success: function (res) {
                if (res.code === 200) {
                    carList = res.data
                    renderList(type)
                }
            }
        });
    }

    /**
     * 渲染列表数据
     * @param list
     */
    function renderList(type) {
        var list = []
        for (var i = 0; i < carList.length; i++) {
            var item = carList[i]
            if (item.carName === type) {
                list.push(item)
            }
        }
        $('.car-list').empty();
        var domList = '';
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            domList += '<div class="car-list-item">\n' +
                '                <img src="' + baseUrl + item.image + '"/>\n' +
                '                <p class="title">' + item.carSeries + '</p>\n' +
                '                <p class="subtitle">厂商建议零售价</p>\n' +
                '                <p class="subtitle">¥' + item.price + '万起</p>\n' +
                '            </div>';
        }
        $('.car-list').html(domList);
    }

    /**
     * 获取URL参数
     * @param variable 参数名
     * @returns {string|boolean}
     */
    function getQueryVariable(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return decodeURIComponent(pair[1]);
            }
        }
        return false;
    }
});
