const BASE_URL = '/api/v1/books'

async function request(path = '', options = {}) {
  let response

  try {
    response = await fetch(`${BASE_URL}${path}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    })
  } catch {
    throw new Error('Connection lost. Is the archive server running?')
  }

  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Record not found.')
    }
    throw new Error(`Archive rejected the request (${response.status}).`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export const booksApi = {
  list: () => request(),
  create: (book) => request('', { method: 'POST', body: JSON.stringify(book) }),
  update: (book) => request('', { method: 'PUT', body: JSON.stringify(book) }),
  remove: (id) => request(`/${id}`, { method: 'DELETE' }),
}
