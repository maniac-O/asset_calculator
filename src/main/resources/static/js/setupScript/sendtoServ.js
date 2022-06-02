let submit = document.querySelector("#setupPriceSubmit");
let valid = document.querySelector("#valid-overload")
let dataSet = {}
let extraDataSet = {}
let extras = []

let extraMap = []


// 항목 추가 버튼
let add = document.querySelector("#add");
let cnt = 0
add.addEventListener('click', ()=>{
    let appended_items = document.querySelector(".appended-items")
    cnt+=1
    appended_items.innerHTML +=
        '<div class="wrap-extra-data wrap-dataset'+cnt+'">'+
        '<select class="dataset'+cnt+' extra-group" name="'+cnt+'"> ' +
        '  <option value="은행">은행</option> ' +
        '  <option value="거래소">거래소</option> ' +
        '  <option value="기타">기타</option> ' +
        '</select>' +
        '  <input class="dataset'+cnt+' extra-key " />' +
        '  <input class="dataset'+cnt+' extra-value " />' +
        '  <button type="button" class="dataset'+cnt+' extra-delete" onclick="deleteExtraItem('+cnt+')">삭제</button>' +
        '  <span class="dataset'+cnt+' extra-percent"></span>' +
        '</div>'


    extraMap.push(cnt)
    calcPercent_small()
    console.log(extraMap)
})

function calcPercent_small(){
    let extra_value = document.querySelectorAll('.extra-value')
    let extra_group = document.querySelectorAll('.extra-group')
    for (let i = 0; i < extra_value.length; i++) {
        extra_value[i].oninput = ()=>{
            calcPercent_update(extraMap[i])
        }
    }
    for (let i = 0; i < extra_group.length; i++) {
        extra_group[i].onchange = ()=>{
            calcPercent_update(extraMap[i])
        }
    }
}

function deleteExtraItem(i){
    let target = document.querySelector('.wrap-dataset'+i)
    target.outerHTML = '';
    extraMap.splice(extraMap.indexOf(i),1)
    calcPercent_small()
}

function calcPercent_update(i){
    let extra_group = document.querySelector('.dataset'+(i)+'.extra-group')
    let base_money;
    let current_money = 0
    let percent = document.querySelector('.dataset'+(i)+'.extra-percent')

    if (extra_group.value === '은행') {
        base_money = parseInt(document.querySelector('#BANKACCOUNT').value)
    }else if (extra_group.value === '거래소') {
        base_money = parseInt(document.querySelector('#MARKET').value)
    }else if (extra_group.value === '기타') {
        base_money = parseInt(document.querySelector('#ETC').value)
    }

    let extra_values = scanExtraValues()
    let extra_groups = scanExtraGroups()


    for (let i = 0; i < extra_groups.length; i++) {
        if(extra_groups[i].value === extra_group.value){
            if (isNaN(extra_values[i].value) || extra_values[i].value.length === 0)
                continue
            current_money += parseInt(extra_values[i].value)
        }
    }

    let cur_value = parseInt(document.querySelector('.dataset'+ (i)+'.extra-value').value)
    if (cur_value.length === 0 || isNaN(cur_value))
        cur_value = 0

    if (base_money.length === 0 || isNaN(base_money)){
        base_money = 0
        percent.innerHTML = '0%'
    }else
        // 여기에 퍼센테지 보여주기
        percent.innerHTML = (cur_value / parseInt(base_money) * 100) + '%'


    // 퍼센트를 넘으면 출력
    if (base_money < current_money) {
        percent.classList.add('overValue')
    }else{
        percent.classList.remove('overValue')
    }
}

function scanExtraValues(){
    return document.querySelectorAll('.extra-value')
}
function scanExtraGroups(){
    return document.querySelectorAll('.extra-group')
}


// 확인 버튼
submit.addEventListener('click', ()=>{
    // dataParsing
    let setupDatas = document.querySelectorAll('.setupData')
    for (let i = 0; i < setupDatas.length; i++) {
        dataSet[setupDatas[i].getAttribute('id')] = setupDatas[i].value
    }

    let extraDatas = document.querySelectorAll('.wrap-extra-data')
    for (let i = 0; i < extraDatas.length; i++) {
        className = extraDatas[i].childNodes[0]['classList'][0]
        let dataSetLocal = document.querySelectorAll('.dataset'+extraMap[i])
        extraDataSet[dataSetLocal[1].value] = [dataSetLocal[0].value, dataSetLocal[2].value]
    }

    let jsonDataArr = [dataSet, extraDataSet]
    let jsonData = JSON.stringify(jsonDataArr);

    if (valid.getAttribute('class') === 'overload' || document.querySelectorAll('.overValue').length > 0){
        alert("값을 확인해주세요")
        extraDataSet.clear()
        return
    }

    let extraKeys = document.querySelectorAll('.extra-key')
    for (let i = 0; i < extraKeys.length; i++) {

        if (extraKeys[i].value.length === 0){
            console.log(isNaN(extraKeys[i].value))
            console.log(extraKeys[i].value.length)
            alert("키 값을 확인해주세요")
            extraDataSet = {}
            return
        }
    }
    let form = document.querySelector('#setupForm')


    $.ajax({
        url: "/setupPrice",
        data: {list: jsonData},
        type: "POST",
        success : function(data){
            console.log('success')
            form.submit()
        },
        error: function(errorThrown) {
            alert("잘못된 요청입니다.");
        }
    });
})