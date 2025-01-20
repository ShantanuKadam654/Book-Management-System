const urlParams = new URLSearchParams(window.location.search);
const bookId = urlParams.get('id');

fetch('/books/api/books/' + bookId)
	.then(response => response.json())
	.then(book => {
		document.getElementById('bookId').textContent = book.bookid;
		document.getElementById('bookName').textContent = book.bookname;
		document.getElementById('bookPrice').textContent = book.bookprice;
		document.getElementById('publishYear').textContent = book.publishyr;
		document.getElementById('bookType').textContent = book.booktype;
		document.getElementById('bookInfo').textContent = book.bookinfo;
		document.getElementById('bookPhoto').src = 'D:\\Sumago\\Img' + book.bookphoto;
	})

const profileBtn = document.getElementById("profileBtn");
const profileModal = document.getElementById("profileModal");
const closeModal = document.getElementById("closeModal");

// Open the modal
profileBtn.onclick = function() {
	profileModal.style.display = "block";
};

// Close the modal
closeModal.onclick = function() {
	profileModal.style.display = "none";
};

// Close modal when clicking outside of it
window.onclick = function(event) {
	if (event.target == profileModal) {
		profileModal.style.display = "none";
	}
};

// Logout function
function logout() {
	window.location.href = "/logout"; // Redirect to logout URL
}

function cart() {
	window.location.href = "/cart";
}

function addToCart(bookId, quantity) {
	// Send a POST request to add the book to the cart
	fetch(`/add_to_cart/${bookId}?quantity=${quantity}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		}
	})
		.then(response => response.text())
		.then(data => {
			alert("Book added to cart!");
		})
		.catch(error => {
			console.error('Error:', error);
			alert("Failed to add book to cart");
		});
}

const paymentstart = () => {
	console.log("Payment started");

	// Get the book price dynamically using its ID
	const amountElement = document.getElementById('bookPrice');
	if (!amountElement) {
		console.error("Price element not found!");
		Swal.fire({
			title: "Error!",
			text: "Unable to fetch book price.",
			icon: "error"
		});
		return;
	}

	const amount = amountElement.textContent.trim();
	console.log("Book Price (amount):", amount);

	if (amount === '' || amount == null) {
		Swal.fire({
			title: "Price Not Available!",
			icon: "error",
			showClass: {
				popup: `animate__animated animate__fadeInUp animate__faster`
			},
			hideClass: {
				popup: `animate__animated animate__fadeOutDown animate__faster`
			}
		});
		return;
	}

	$.ajax({
		url: '/create_order',
		data: JSON.stringify({ amount: amount, info: 'order_request' }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response) {
			console.log(response);

			if (response.status === "created") {
				var options = {
					"key": 'rzp_test_etuDi8WEJCyOzZ',
					"amount": response.amount,
					"currency": "INR",
					"name": "BookLedger",
					"description": "Book Payment",
					"image": "http://localhost:8080/video/BL.png",
					"order_id": response.id,

					handler: function(response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						Swal.fire({
							position: "center",
							icon: "success",
							title: "Payment Successful",
							showConfirmButton: false,
							timer: 3500
						});
					},
					"prefill": {
						"name": "",
						"email": "",
						"contact": ""
					},
					"notes": {
						"address": "Welcome to BookLedger"
					},
					"theme": {
						"color": "#3399cc"
					}
				};

				var rzp1 = new Razorpay(options);

				rzp1.on('payment.failed', function(response) {
					console.error("Payment failed:", response);
					Swal.fire({
						title: "Payment Failed!",
						icon: "error",
						text: response.error.description,
						showClass: {
							popup: `animate__animated animate__fadeInUp animate__faster`
						},
						hideClass: {
							popup: `animate__animated animate__fadeOutDown animate__faster`
						}
					});
				});
				rzp1.open();
			}
		},
		error: function(error) {
			console.error("Error occurred:", error);
			Swal.fire({
				title: "Error Occurred!",
				icon: "error",
				text: "Unable to process payment. Please try again later.",
			});
		}
	});
};

