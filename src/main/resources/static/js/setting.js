
let splitButtons = document.querySelectorAll(".splitButton")

let buckets = document.querySelector(".buckets");
appending(1);

for (const splitButton of splitButtons) {
    splitButton.addEventListener('click', () => {
        for (const splitButton2 of splitButtons) {
            splitButton2.classList.remove('onClickButton');
        }
        splitButton.classList.add('onClickButton');

        let number = splitButton.getAttribute('id').substr(-1);

        document.querySelector('#split').value = number;
        appending(number);
    });
}

function appending(base){

    let bucket = document.querySelectorAll(".bucket");
    let bucketLabel = document.querySelectorAll(".bucket-label");

    for (const bucketElement of bucket) {
        bucketElement.remove();
    }
    for (const bucketLabelElement of bucketLabel) {
        bucketLabelElement.remove();
    }

    for (let i = 0; i < base; i++) {
        buckets.innerHTML+= '<div class="bucket-label wrap-span"><span>분할'+(i+1)+'</span></div>';
        buckets.innerHTML+= '<div class="bucket" id="'+i+'" onDragOver="allowDrop();" onDrop="dropItem(event);"></div>';
    }
}
