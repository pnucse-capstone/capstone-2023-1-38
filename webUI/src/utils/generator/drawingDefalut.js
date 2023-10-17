export default [
  {
    layout: 'colFormItem',
    tagIcon: 'input',
    label: '전화번호',
    vModel: 'mobile',
    formId: 6,
    tag: 'el-input',
    placeholder: '전화번호를 입력하세요',
    defaultValue: '',
    span: 24,
    style: { width: '100%' },
    clearable: true,
    prepend: '',
    append: '',
    'prefix-icon': 'el-icon-mobile',
    'suffix-icon': '',
    maxlength: 11,
    'show-word-limit': true,
    readonly: false,
    disabled: false,
    required: true,
    changeTag: true,
    regList: [{
      pattern: /^01[0-9]{8,9}$/,
      message: "Please enter the correct phone number",
    }]
  }
]
