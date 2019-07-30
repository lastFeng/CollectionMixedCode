function selected() {
    // 获取被选中的option标签
    var element = document.getElementById("selectByName");
    var cityName = element.options[element.selectedIndex].value;
    console.log(cityName);
}