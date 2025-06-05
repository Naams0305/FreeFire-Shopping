<%@page import="com.mycompany.mproject.model.Cart"%>
<%@page import="com.mycompany.mproject.model.CartItem"%>
<%@page import="com.mycompany.mproject.model.User"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh toán - OtterShop</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/CheckoutCSS.css">
    </head>
    <body>
        <!-- Header -->
        <div class="header">
            <div class="text-header">
                <img src="${pageContext.request.contextPath}/images/otter.png" alt="OtterShop"/>
                <h1>OtterShop</h1>
            </div>
            <div class="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/Home">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/Product">Danh sách sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/Cart">Giỏ hàng</a></li>
                </ul>
            </div>
        </div>

        <div class="content">
            <div class="checkout-container">
                <h2>Thanh toán đơn hàng</h2>

                <!-- Progress Steps -->
                <div class="checkout-steps">
                    <div class="step active">
                        <span class="step-number">1</span>
                        <span class="step-title">Thông tin giao hàng</span>
                    </div>
                    <div class="step">
                        <span class="step-number">2</span>
                        <span class="step-title">Phương thức thanh toán</span>
                    </div>
                    <div class="step">
                        <span class="step-number">3</span>
                        <span class="step-title">Xác nhận đơn hàng</span>
                    </div>
                </div>

                <!-- Thông báo lỗi -->
                <% if (request.getAttribute("errorMessage") != null) {%>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage")%>
                </div>
                <% } %>

                <%-- Lấy đối tượng User ra một cách an toàn --%>
                <%
                    User user = (User) session.getAttribute("userLogin");
                    String userName = (user != null) ? user.getName() : "";
                    String userEmail = (user != null) ? user.getEmail() : "";
                %>

                <form action="${pageContext.request.contextPath}/Checkout" method="post" id="checkoutForm">
                    <div class="checkout-content">
                        <!-- Thông tin giao hàng -->
                        <div class="checkout-section" id="shipping-info">
                            <h3>Thông tin giao hàng</h3>
                            <div class="form-group">
                                <label for="fullName">Họ và tên *</label>
                                <%-- Sử dụng biến đã được kiểm tra null --%>
                                <input type="text" id="fullName" name="fullName" required 
                                       value="<%= userName%>">
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="phone">Số điện thoại *</label>
                                    <input type="tel" id="phone" name="phone" required>
                                </div>
                                <div class="form-group">
                                    <label for="email">Email *</label>
                                    <%-- Sử dụng biến đã được kiểm tra null --%>
                                    <input type="email" id="email" name="email" required 
                                           value="<%= userEmail%>">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="address">Địa chỉ giao hàng *</label>
                                <textarea id="address" name="address" rows="3" required placeholder="Nhập địa chỉ chi tiết..."></textarea>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="city">Tỉnh/Thành phố *</label>
                                    <select id="city" name="city" required>
                                        <option value="">Chọn tỉnh/thành phố</option>
                                        <option value="Bình Định">Bình Định</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="district">Quận/Huyện *</label>
                                    <select id="district" name="district" required>
                                        <option value="">Chọn quận/huyện</option>
                                        <option value="Quy Nhơn">Quy Nhơn</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="notes">Ghi chú</label>
                                <textarea id="notes" name="notes" rows="2" placeholder="Ghi chú thêm cho đơn hàng..."></textarea>
                            </div>
                        </div>

                        <!-- Phương thức thanh toán -->
                        <div class="checkout-section" id="payment-method" style="display: none;">
                            <h3>Phương thức thanh toán</h3>
                            <div class="payment-options">
                                <div class="payment-option">
                                    <input type="radio" id="cod" name="paymentMethod" value="cod" checked>
                                    <label for="cod">
                                        <div class="payment-info">
                                            <strong>Thanh toán khi nhận hàng (COD)</strong>
                                            <p>Thanh toán bằng tiền mặt khi nhận hàng</p>
                                        </div>
                                    </label>
                                </div>

                                <div class="payment-option">
                                    <input type="radio" id="bank-transfer" name="paymentMethod" value="bank-transfer">
                                    <label for="bank-transfer">
                                        <div class="payment-info">
                                            <strong>Chuyển khoản ngân hàng</strong>
                                            <p>Chuyển khoản qua ngân hàng hoặc ví điện tử</p>
                                        </div>
                                    </label>
                                </div>

                                <div class="payment-option">
                                    <input type="radio" id="credit-card" name="paymentMethod" value="credit-card">
                                    <label for="credit-card">
                                        <div class="payment-info">
                                            <strong>Thẻ tín dụng/Ghi nợ</strong>
                                            <p>Thanh toán trực tuyến bằng thẻ</p>
                                        </div>
                                    </label>
                                </div>
                            </div>

                            <!-- Chi tiết thanh toán thẻ -->
                            <div class="card-details" id="card-details" style="display: none;">
                                <div class="form-group">
                                    <label for="cardNumber">Số thẻ *</label>
                                    <input type="text" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456">
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="expiryDate">Ngày hết hạn *</label>
                                        <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/YY">
                                    </div>
                                    <div class="form-group">
                                        <label for="cvv">CVV *</label>
                                        <input type="text" id="cvv" name="cvv" placeholder="123">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="cardName">Tên trên thẻ *</label>
                                    <input type="text" id="cardName" name="cardName" placeholder="Tên như trên thẻ">
                                </div>
                            </div>
                        </div>

                        <!-- Xác nhận đơn hàng -->
                        <div class="checkout-section" id="order-review" style="display: none;">
                            <h3>Xác nhận đơn hàng</h3>
                            <div class="order-summary">
                                <h4>Thông tin giao hàng</h4>
                                <div class="summary-info" id="shipping-summary"></div>

                                <h4>Phương thức thanh toán</h4>
                                <div class="summary-info" id="payment-summary"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Sidebar đơn hàng -->
                    <div class="order-sidebar">
                        <div class="order-summary-box">
                            <h3>Đơn hàng của bạn</h3>

                            <%
                                Cart cart = (Cart) request.getAttribute("cart");
                                if (cart == null) {
                                    cart = (Cart) session.getAttribute("cart");
                                }

                                int shippingFee = 30000; // Phí ship cố định 30k
                                long totalWithShipping = cart.getTotalAmount() + shippingFee;
                            %>

                            <div class="order-items">
                                <% for (CartItem item : cart.getItems()) {%>
                                <div class="order-item">
                                    <img src="${pageContext.request.contextPath}/<%= item.getProductImage()%>" 
                                         alt="<%= item.getProductName()%>" class="item-thumb">
                                    <div class="item-info">
                                        <h5><%= item.getProductName()%></h5>
                                        <p>Số lượng: <%= item.getQuantity()%></p>
                                    </div>
                                    <div class="item-price">
                                        <%= String.format("%,d", item.getTotalPrice())%> VNĐ
                                    </div>
                                </div>
                                <% }%>
                            </div>

                            <div class="order-totals">
                                <div class="total-row">
                                    <span>Tạm tính:</span>
                                    <span><%= String.format("%,d", cart.getTotalAmount())%> VNĐ</span>
                                </div>
                                <div class="total-row">
                                    <span>Phí vận chuyển:</span>
                                    <span><%= String.format("%,d", shippingFee)%> VNĐ</span>
                                </div>
                                <div class="total-row final-total">
                                    <span>Tổng cộng:</span>
                                    <span><%= String.format("%,d", totalWithShipping)%> VNĐ</span>
                                </div>
                            </div>
                        </div>

                        <!-- Navigation buttons -->
                        <div class="checkout-navigation">
                            <button type="button" id="prevBtn" onclick="changeStep(-1)" style="display: none;">Quay lại</button>
                            <button type="button" id="nextBtn" onclick="changeStep(1)">Tiếp tục</button>
                            <button type="submit" id="submitBtn" style="display: none;">Đặt hàng</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <p>&copy; 2025 OtterShop. Tất cả quyền được bảo lưu.</p>
        </div>

        <script>
            let currentStep = 1;
            const totalSteps = 3;

            function changeStep(direction) {
                if (direction === 1 && !validateCurrentStep()) {
                    return;
                }

                currentStep += direction;

                if (currentStep < 1)
                    currentStep = 1;
                if (currentStep > totalSteps)
                    currentStep = totalSteps;

                showStep(currentStep);
                updateStepIndicator();
                updateNavigation();

                if (currentStep === 3) {
                    updateOrderSummary();
                }
            }

            function showStep(step) {
                document.querySelectorAll('.checkout-section').forEach(section => {
                    section.style.display = 'none';
                });

                switch (step) {
                    case 1:
                        document.getElementById('shipping-info').style.display = 'block';
                        break;
                    case 2:
                        document.getElementById('payment-method').style.display = 'block';
                        break;
                    case 3:
                        document.getElementById('order-review').style.display = 'block';
                        break;
                }
            }

            function updateStepIndicator() {
                document.querySelectorAll('.step').forEach((step, index) => {
                    step.classList.remove('active', 'completed');
                    if (index + 1 < currentStep) {
                        step.classList.add('completed');
                    } else if (index + 1 === currentStep) {
                        step.classList.add('active');
                    }
                });
            }

            function updateNavigation() {
                const prevBtn = document.getElementById('prevBtn');
                const nextBtn = document.getElementById('nextBtn');
                const submitBtn = document.getElementById('submitBtn');

                prevBtn.style.display = currentStep > 1 ? 'block' : 'none';
                nextBtn.style.display = currentStep < totalSteps ? 'block' : 'none';
                submitBtn.style.display = currentStep === totalSteps ? 'block' : 'none';
            }

            function validateCurrentStep() {
                switch (currentStep) {
                    case 1:
                        return validateShippingInfo();
                    case 2:
                        return validatePaymentMethod();
                    default:
                        return true;
                }
            }

            function validateShippingInfo() {
                const required = ['fullName', 'phone', 'email', 'address', 'city', 'district'];
                for (let field of required) {
                    const element = document.getElementById(field);
                    if (!element.value.trim()) {
                        element.focus();
                        alert('Vui lòng điền đầy đủ thông tin giao hàng!');
                        return false;
                    }
                }
                return true;
            }

            function validatePaymentMethod() {
                const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
                if (paymentMethod === 'credit-card') {
                    const cardFields = ['cardNumber', 'expiryDate', 'cvv', 'cardName'];
                    for (let field of cardFields) {
                        const element = document.getElementById(field);
                        if (!element.value.trim()) {
                            element.focus();
                            alert('Vui lòng điền đầy đủ thông tin thẻ!');
                            return false;
                        }
                    }
                }
                return true;
            }

            function updateOrderSummary() {
                // Bước 1: Lấy tất cả giá trị từ form và lưu vào các biến CÓ TÊN KHÁC
                const nameValue = document.getElementById('fullName').value;
                const phoneValue = document.getElementById('phone').value;
                const addressValue = document.getElementById('address').value;
                const districtValue = document.getElementById('district').value;
                const cityValue = document.getElementById('city').value;

                // (Tùy chọn) Bạn có thể giữ lại dòng console.log để kiểm tra
                console.log("Dữ liệu đã lấy từ form:", {
                    name: nameValue,
                    phone: phoneValue,
                    address: addressValue,
                    district: districtValue,
                    city: cityValue
                });

                // Bước 2: Cập nhật thông tin giao hàng vào UI bằng các biến MỚI
                const shippingSummary = document.getElementById('shipping-summary');
                shippingSummary.innerHTML = `
        <p><strong>Người nhận:</strong> ${nameValue}</p>
        <p><strong>Điện thoại:</strong> ${phoneValue}</p>
        <p><strong>Địa chỉ:</strong> ${addressValue}, ${districtValue}, ${cityValue}</p>
    `;

                // Bước 3: Cập nhật phương thức thanh toán (giữ nguyên như cũ)
                const paymentSummary = document.getElementById('payment-summary');
                const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
                const paymentText = paymentMethod.parentElement.querySelector('strong').textContent;
                paymentSummary.innerHTML = `<p><strong>${paymentText}</strong></p>`;
            }

            // Xử lý thay đổi phương thức thanh toán
            document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
                radio.addEventListener('change', function () {
                    const cardDetails = document.getElementById('card-details');
                    cardDetails.style.display = this.value === 'credit-card' ? 'block' : 'none';
                });
            });

            // Format số thẻ
            document.getElementById('cardNumber').addEventListener('input', function (e) {
                let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
                let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
                e.target.value = formattedValue;
            });

            // Format ngày hết hạn
            document.getElementById('expiryDate').addEventListener('input', function (e) {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length >= 2) {
                    value = value.substring(0, 2) + '/' + value.substring(2, 4);
                }
                e.target.value = value;
            });

            // Chỉ cho phép số CVV
            document.getElementById('cvv').addEventListener('input', function (e) {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
            });

            // Khởi tạo
            showStep(1);
            updateNavigation();
        </script>
    </body>
</html>