const form = document.getElementById("form");
const fullName = document.getElementById("name");
const email = document.getElementById("email");
const password = document.getElementById("password");
const confirmpw = document.getElementById("confirmPw");
const address = document.getElementById("address");
const phoneNo = document.getElementById("number");

form.addEventListener('submit', function(e){
    e.preventDefault();

    if (validateInputs()) {
        this.submit();
    }
});


// Error Message
const setError = (element, message) => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success');
}


//success message
const setSuccess = element => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
}


//email validation
const isValidEmail = email => {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

//check for strong password
const isValidPassword = password => {
    const pw =  /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{7,15}$/;
    return pw.test(String(password).toLowerCase());
}


//check for phone number
function isValidPhoneNumber(phoneNumber) {
    // Regular expression to match 10 digits
    var regex = /^\d{10}$/;

    // Test the phone num against the regex
    return regex.test(phoneNumber);
}

const validateInputs = () => {
    const emailValue = email.value.trim();
    const nameValue = fullName.value;
    const passwordValue = password.value.trim();
    const confirmpwValue = confirmpw.value.trim();
    const addressValue = address.value;
    const phoneValue = phoneNo.value.trim();

    let isValid = true;

    //check email validation
    if(emailValue === '') {
        setError(email, 'Email is required');
        isValid=false;
    } else if(!isValidEmail(emailValue)) {
        setError(email, 'Insert valid email address');
        isValid=false;
    } else {
        setSuccess(email);
    }


    //check phone validation
    if(phoneValue === '') {
        setError(phoneNo, 'Phone is required');
        isValid=false;
    } else if(!isValidPhoneNumber(phoneValue)) {
        setError(phoneNo, 'Phone number must contain 10 digits');
        isValid=false;
    } else {
        setSuccess(phoneNo);
    }


    //check for name validation
    if (nameValue === '') {
        setError(fullName, 'Name is required');
        isValid=false;
    } else {
        setSuccess(fullName);
    }

    //check for address validation
    if (addressValue === '') {
        setError(address, 'Address is required');
        isValid=false;
    } else {
        setSuccess(address);
    }

    //check password validation
    if(passwordValue === '') {
        setError(password, 'Password is required');
        isValid=false;
    } else if(passwordValue.length < 7) {
        setError(password,'Password must be 7 characters');
        isValid=false;
    } else if (!isValidPassword(passwordValue)) {
        setError(password,'Please insert strong password');
        isValid=false;
    } else {
        setSuccess(password);
    }

    //check for matching password
    if(confirmpwValue === '') {
        setError(confirmpw, 'Please confirm your password');
        isValid=false;
    } else if(confirmpwValue !== passwordValue) {
        setError(confirmpw, "Password doesn't match");
        isValid=false;
    } else {
        setSuccess(confirmpw);
    }
    return isValid;
}
