
window.addEventListener('scroll', ()=>{
    if((window.innerHeight + window.scrollY) >= document.body.offsetHeight){
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
                //changeIndex(lastIndex, newIndex)
            },
            error: function(errorThrown) {
                alert("잘못된 요청입니다.");
            }
        });
    }
})

// change old index to new index
function changeIndex(lastIndex, newIndex){
    lastIndex.id = newIndex;
}