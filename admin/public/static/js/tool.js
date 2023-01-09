Tool = {

  isEmpty: function (obj) {
    if ((typeof obj == 'string')) {
      return !obj || obj.replace(/\s+/g, "") == ""
    } else {
      return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
    }
  },

  isNotEmpty: function (obj) {
    return !this.isEmpty(obj);
  },

  isLength: function (str, min, max) {
    return $.trim(str).length >= min && $.trim(str).length <= max;
  },

  /**
   * Time format, take the current time when date is empty
   */
  dateFormat: function (format, date) {
    let result;
    if (!date) {
      date = new Date();
    }
    const option = {
      "y+": date.getFullYear().toString(),        // year
      "M+": (date.getMonth() + 1).toString(),     // month
      "d+": date.getDate().toString(),            // day
      "h+": date.getHours().toString(),           // hour
      "m+": date.getMinutes().toString(),         // minute
      "s+": date.getSeconds().toString()          // second
    };
    for (let i in option) {
      result = new RegExp("(" + i + ")").exec(format);
      if (result) {
        format = format.replace(result[1], (result[1].length == 1) ? (option[i]) : (option[i].padStart(result[1].length, "0")))
      }
    }
    return format;
  },

  /**
   * @param array
   * @param obj
   * @returns {number}
   */
  removeObj: function (array, obj) {
    let index = -1;
    for (let i = 0; i < array.length; i++) {
      if (array[i] === obj) {
        array.splice(i, 1);
        index = i;
        break;
      }
    }
    return index;
  },

  /**
   * Decimal to 62 hexadecimal
   * @param number
   * @returns {string}
   * @private
   */
  _10to62: function (number) {
    let chars = '0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ';
    let radix = chars.length;
    let arr = [];
    do {
      let mod = number % radix;
      number = (number - mod) / radix;
      arr.unshift(chars[mod]);
    } while (number);
    return arr.join('');
  },

  /**
   * Save login user information
   */
  setLoginUser: function (loginUser) {
    SessionStorage.set(SESSION_KEY_LOGIN_USER, loginUser);
  },

  /**
   * Get login user information
   */
  getLoginUser: function () {
    return SessionStorage.get(SESSION_KEY_LOGIN_USER) || {};
  },

  /**
   * Randomly generate [radix] base numbers of [len] length
   * @param len
   * @param radix 
   * @returns {string}
   */
  uuid: function (len, radix) {
    let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    let uuid = [];
    radix = radix || chars.length;

    for (let i = 0; i < len; i++) {
      uuid[i] = chars[0 | Math.random() * radix];
    }

    return uuid.join('');
  },

  /**
   * Find out if you have permission
   * @param id 
   */
  hasResource: function (id) {
    let _this = this;
    let resources = _this.getLoginUser().resources;
    if (_this.isEmpty(resources)) {
      return false;
    }
    for (let i = 0; i < resources.length; i++) {
      if (id === resources[i].id) {
        return true;
      }
    }
    return false;
  }
};