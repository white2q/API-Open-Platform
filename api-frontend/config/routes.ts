export default [
  {name: '登录', path: '/user', layout: false, routes: [{path: '/user/login', component: './User/Login'}]},
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    name: '管理员页面',
    routes: [
      {name: '接口管理', icon: 'table', path: '/admin/interface_info', component: './InterfaceInfo'},
    ],
  },
  // {path: '/', redirect: '/list'},
  {path: '*', layout: false, component: './404'},
];
