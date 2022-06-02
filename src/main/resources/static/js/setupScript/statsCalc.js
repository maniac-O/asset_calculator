
let overload = document.querySelector("#valid-overload")

let finalTotal = document.querySelector("#FINALTOTAL")
let finalTotal_percent = document.querySelector("#FINALTOTAL_percent")

let bankAccount = document.querySelector("#BANKACCOUNT")
let bankAccount_percent = document.querySelector("#BANKACCOUNT_percent")

let market = document.querySelector("#MARKET")
let market_percent = document.querySelector("#MARKET_percent")

let etc = document.querySelector("#ETC")
let etc_percent = document.querySelector("#ETC_percent")

bankAccount.disabled = true
market.disabled = true
etc.disabled = true

finalTotal.oninput = ()=>{
    if (isNaN(finalTotal.value) || finalTotal.value.length === 0){
        bankAccount.disabled = true
        market.disabled = true
        etc.disabled = true
    }else{
        bankAccount.disabled = false
        market.disabled = false
        etc.disabled = false
    }

    calcBankAccount()
    calcMarket()
    calcEtc()
}

// sendtoServ에 있는 놈을 재호출
bankAccount.oninput = ()=>{
    calcBankAccount()
}

market.oninput = ()=>{
    calcMarket()
}

etc.oninput = ()=>{
    calcEtc()
}

function calcBankAccount(){
    if (!calcPercent()){
        bankAccount_percent.classList.add('overValue');
    }
    for (let i = 0; i < document.querySelectorAll('.extra-value').length; i++) {
        calcPercent_update(extraMap[i])
    }
}

function calcMarket(){

    if (!calcPercent()){
        market_percent.classList.add('overValue');
    }
    for (let i = 0; i < document.querySelectorAll('.extra-value').length; i++) {
        calcPercent_update(extraMap[i])
    }
}

function calcEtc(){

    if (!calcPercent()){
        etc_percent.classList.add('overValue');
    }
    for (let i = 0; i < document.querySelectorAll('.extra-value').length; i++) {
        calcPercent_update(extraMap[i])
    }
}
function calcPercent(){
    final_value = parseInt(finalTotal.value)
    bank_value = parseInt(bankAccount.value)
    market_value = parseInt(market.value)
    etc_value = parseInt(etc.value)

    none = false

    if (isNaN(bank_value) || bank_value.length === 0)
        bank_value = 0
    if (isNaN(market_value) || market_value.length === 0)
        market_value = 0
    if (isNaN(etc_value) || etc_value.length === 0)
        etc_value = 0

    if (isNaN(final_value) || final_value.length === 0){
        final_value = 1
        none = true
        overload.classList.add('overload')
        let percent = document.querySelectorAll('.percent')
        for (const percentElement of percent) {
            percentElement.classList.add('overValue')
        }
    }

    bankAccount_percent.innerHTML = (bank_value / final_value * 100) + '%'
    market_percent.innerHTML = (market_value / final_value * 100) + '%'
    etc_percent.innerHTML = (etc_value / final_value * 100) + '%'

    if ((bank_value + market_value + etc_value) > parseInt(finalTotal.value) || none){
        overload.classList.add('overload')
        return false
    }else{
        overload.classList.remove('overload')
        let overValued = document.querySelectorAll('.overValue')
        for (const overValuedElement of overValued) {
            overValuedElement.classList.remove('overValue')
        }
        return true
    }
}



