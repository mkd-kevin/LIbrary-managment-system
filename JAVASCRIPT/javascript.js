function getOddNumbers(numbers) {
    return numbers.filter(num => num % 2 !== 0);
}

function multiplyNumbers(numbers) {
    return numbers.reduce((total, num) => total * num, 1);
}

let numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13];

console.log("this is the array of Odd numbers:", getOddNumbers(numbers));

console.log("the Multiplication is :", multiplyNumbers(numbers));