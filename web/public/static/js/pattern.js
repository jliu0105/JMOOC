Pattern = {
  // Regular username, 2 to 16 characters (letters, numbers, underscores)
  loginNamePattern: /^[a-zA-Z0-9_]{6,16}$/,

  // Regular nickname, 6 to 20 characters, letters, numbers, underscores
  namePattern: /^[\w\u4e00-\u9fa5]{2,20}$/,

  // Strong password strength regular, at least 8 characters, including at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character
  passwordStrongPattern: /^.*(?=.{8,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/,

  // Weak password strength regular, at least 6 characters, including at least 1 letter and 1 number
  passwordWeakPattern: /^.*(?=.{6,})(?=.*\d)(?=.*[A-Za-z]).*$/,

  // Regular mobile phone number, 11 digits, beginning with 1
  mobilePattern: /^1\d{10}$/,

  // The picture verification code is regular, 4 letters, numbers
  imageCodePattern: /^[a-zA-Z0-9]{4}$/,

  // Mobile phone verification code is regular, 6 digits
  mobileCodePattern: /^[0-9]{6}$/,

  validateLoginName: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.loginNamePattern.test(str);
  },

  validateName: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.namePattern.test(str);
  },

  validatePasswordStrong: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.passwordStrongPattern.test(str);
  },

  validatePasswordWeak: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.passwordWeakPattern.test(str);
  },

  validateMobile: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.mobilePattern.test(str);
  },

  validateImageCode: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.imageCodePattern.test(str);
  },

  validateMobileCode: function (str) {
    if (Tool.isEmpty(str)) {
      return false;
    }
    return this.mobileCodePattern.test(str);
  }
};
