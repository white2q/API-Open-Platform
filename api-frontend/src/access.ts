/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: InitialState) {
  const { loginUser } = initialState ?? {};
  return {
    canAdmin: loginUser && loginUser.userRole === 'admin',
    canUser: loginUser && loginUser.userRole === 'user',
  };
}
