import Vue from 'vue'
import {
  Alert,
  Button,
  Cascader, Checkbox, CheckboxGroup,
  Dialog,
  Option,
  Pagination,
  Select, Step, Steps,
  Switch,
  TabPane,
  Tabs,
  Tag, Timeline, TimelineItem,
  Tooltip,
  Tree, Upload,
  Autocomplete
} from 'element-ui'
import { Form, FormItem } from 'element-ui'
import { Input, InputNumber } from 'element-ui'
import { Message, MessageBox } from 'element-ui'
import { Container, Header, Aside, Main } from 'element-ui'
import { Menu, Submenu, MenuItem } from 'element-ui'
import { Breadcrumb, BreadcrumbItem, Card, Row, Col, Table, TableColumn } from 'element-ui'
import { RadioGroup, Radio } from 'element-ui'

Vue.use(Button)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Input)
Vue.use(InputNumber)

Vue.use(Container)
Vue.use(Header)
Vue.use(Aside)
Vue.use(Main)

Vue.use(Menu)
Vue.use(Submenu)
Vue.use(MenuItem)

Vue.use(Breadcrumb)
Vue.use(BreadcrumbItem)
Vue.use(Card)
Vue.use(Row)
Vue.use(Col)
Vue.use(TableColumn)
Vue.use(Table)
Vue.use(Switch)
Vue.use(Tooltip)
Vue.use(Pagination)
Vue.use(Dialog)
Vue.use(Tag)
Vue.use(Tree)
Vue.use(Select)
Vue.use(Option)
Vue.use(Cascader)
Vue.use(Alert)
Vue.use(Tabs)
Vue.use(TabPane)
Vue.use(Steps)
Vue.use(Step)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)
Vue.use(Upload)
Vue.use(Timeline)
Vue.use(TimelineItem)
Vue.use(RadioGroup)
Vue.use(Radio)
Vue.use(Autocomplete)
Vue.prototype.$message = Message
Vue.prototype.$confirm = MessageBox.confirm
