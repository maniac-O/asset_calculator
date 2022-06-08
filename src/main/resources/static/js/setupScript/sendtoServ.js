let submit = document.querySelector("#setupPriceSubmit");
let valid = document.querySelector("#valid-overload")
let dataSet = {}
let extraDataSet = {}
let extras = []
let percentSet = {}
let extraPercentSet = {}

let extraMap = []

let cnt = 0

let add_buttons = document.querySelectorAll(".addButton")
addElementButton()
function addElementButton() {
    for (const button of add_buttons) {
        button.addEventListener('click', () => {
            let target = document.querySelector('#' + button.id).id.split('_')[0]
            let target_html = document.querySelector('#' + target+'_child')
            cnt += 1
            target_html.innerHTML +=
                '<div class="wrap-extra-data wrap-dataset' + cnt + '">' +
                '<input class="dataset' + cnt + ' extra-group" name="' + cnt + '" disabled="true" value="' + target + '"> ' +
                '<input class="dataset' + cnt + ' extra-key " />' +
                '<input type="number"  class="dataset' + cnt + ' extra-value '+ target +'-value " />' +
                '<button type="button" class="dataset' + cnt + ' extra-delete" onclick="deleteExtraItem(' + cnt + ')">삭제</button>' +
                '<span class="dataset' + cnt + ' extra-percent"></span>' +
                '</div>'

            extraMap.push(cnt)
            console.log(cnt)
            calcPercent_small()
        })
    }
}



/*
// (구)항목 추가 버튼
let add = document.querySelector("#add");
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
        '  <input class="dataset'+cnt+' extra-value  " />' +
        '  <button type="button" class="dataset'+cnt+' extra-delete" onclick="deleteExtraItem('+cnt+')">삭제</button>' +
        '  <span class="dataset'+cnt+' extra-percent"></span>' +
        '</div>'


    extraMap.push(cnt)
    calcPercent_small()
})

 */

function calcPercent_small(){
    let extra_value = document.querySelectorAll('.extra-value')
    //let extra_group = document.querySelectorAll('.extra-group')
    for (let i = 0; i < extra_value.length; i++) {
        extra_value[i].oninput = ()=>{
            targets = document.querySelectorAll('.'+extra_value[i].className.split(' ')[2]);
            for (const target of targets) {
                //console.log(target.className.split(' ')[0].substr(-1))
                calcPercent_update(target.className.split(' ')[0].substr(-1))
            }
            // calcPercent_update(extraMap[i])
        }
    }
    /*
    for (let i = 0; i < extra_group.length; i++) {
        extra_group[i].onchange = ()=>{
            calcPercent_update(extraMap[i])
        }
    }

     */
}

function deleteExtraItem(i){
    let target = document.querySelector('.wrap-dataset'+i)
    target.outerHTML = '';
    extraMap.splice(extraMap.indexOf(i),1)
    calcPercent_small()
}

function calcPercent_update(i){

    let extra_group = document.querySelector('.dataset'+(i)+'.extra-group')
    let base_money = 0;
    let current_money = 0
    let percent = document.querySelector('.dataset'+(i)+'.extra-percent')

    let group_values = document.querySelectorAll('.'+ extra_group.value +'-value')

    // 현재 그룹의 총 자산 계산
    for (const groupValue of group_values) {
        let value = parseInt(groupValue.value);
        if( isNaN(value) || value.length === 0)
            value = 0

        base_money += value
    }
    document.querySelector('#'+extra_group.value).value = base_money


    if (base_money == 0)
        base_money = 1

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
        percent.innerHTML = Math.floor(cur_value / parseInt(base_money) * 100) + '%'

    // ==========================================================


    // 전체 총 자산 계산
    let manual_fields = document.querySelectorAll('.setupData');
    let total = 0
    for (let j = 1; j < manual_fields.length; j++) {
        let value = parseInt(manual_fields[j].value);
        if( isNaN(value) || value.length === 0)
            value = 0

        total += value
    }
    manual_fields[0].value = total
    for (let j = 1; j < manual_fields.length; j++) {

        let value = parseInt(manual_fields[j].value);
        let target = document.querySelector('#' + manual_fields[j].id + '_percent');

        if( isNaN(value) || value.length === 0)
            value = 0

        target.innerHTML = Math.floor(value / total * 100) + '%';
    }


    /*
    // 퍼센트를 넘으면 출력
    if (base_money < current_money) {
        percent.classList.add('overValue')
    }else{
        percent.classList.remove('overValue')
    }

     */
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
    let percents = document.querySelectorAll('.percent')
    for (let i = 0; i < setupDatas.length; i++) {
        dataSet[setupDatas[i].getAttribute('id')] = setupDatas[i].value
        // FINALTOTAL은 퍼센트가 없으므로 무시
        if (i+1===setupDatas.length)
            continue
        percentSet[setupDatas[i].getAttribute('id')] = percents[i].textContent.substr(0, percents[i].textContent.length-1)
    }

    let extraDatas = document.querySelectorAll('.wrap-extra-data')
    for (let i = 0; i < extraDatas.length; i++) {
        // finance scale
        let dataSetLocal = document.querySelectorAll('.dataset'+extraMap[i])
        // percent
        let percent = document.querySelector('.dataset' + extraMap[i]+'.extra-percent').textContent;

        extraDataSet[dataSetLocal[1].value] = [dataSetLocal[0].value, dataSetLocal[2].value]
        extraPercentSet[dataSetLocal[1].value] = percent.substr(0, percent.length-1)
    }

    let jsonDataArr = [dataSet, extraDataSet, percentSet, extraPercentSet]
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
    console.log(jsonData)

    $.ajax({
        url: "/setupprice",
        data: {list: jsonData},
        type: "POST",
        success : function(data){
            console.log('success')
            //form.submit()
        },
        error: function(errorThrown) {
            alert("잘못된 요청입니다.");
        }
    });
})