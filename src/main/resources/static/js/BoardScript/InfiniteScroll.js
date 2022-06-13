let scrollDetected = false
window.addEventListener('scroll', ()=>{
    if((window.innerHeight + window.scrollY) >= document.body.offsetHeight && !scrollDetected){
        scrollDetected = true
        let lastIndex = document.querySelector('.last-index')

        console.log(lastIndex.id);
        $.ajax({
            url: "/morepage",
            data: 'lastIndex='+lastIndex.id,
            type: "GET",
            success : function(data){
                console.log('success')
                console.log(data);
                // todo 파싱부터 해야할듯?
                addData(data)
                //changeIndex(lastIndex, newIndex)
            },
            error: function(errorThrown) {
                alert("잘못된 요청입니다.");
            },
            complete: function () {
                scrollDetected = false;
            }
        });
    }
})

function addData(data){
    let target = document.querySelector('#openboard_target')
    //target.innerHTML +=
}

// change old index to new index
function changeIndex(lastIndex, newIndex){
    lastIndex.id = newIndex;
}