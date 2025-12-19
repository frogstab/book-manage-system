// Token管理
export const setToken = (token) => {
  sessionStorage.setItem('token', token)
}

export const getToken = () => {
  return sessionStorage.getItem('token')
}

export const removeToken = () => {
  sessionStorage.removeItem('token')
}

export const clearToken = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('role')
  sessionStorage.removeItem('userName')
  sessionStorage.removeItem('userId')
}

// 角色管理
export const setRole = (role) => {
  sessionStorage.setItem('role', role.toString())
}

export const getRole = () => {
  return sessionStorage.getItem('role')
}

// 用户信息管理
export const setUserInfo = (userInfo) => {
  sessionStorage.setItem('userName', userInfo.name || '')
  sessionStorage.setItem('userId', userInfo.id || '')
}

export const getUserInfo = () => {
  return {
    name: sessionStorage.getItem('userName'),
    id: sessionStorage.getItem('userId')
  }
}