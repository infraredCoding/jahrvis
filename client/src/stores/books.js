import { booksApi } from '@/api/books'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useBooksStore = defineStore('books', () => {
  const books = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function run(action) {
    loading.value = true
    error.value = null

    try {
      return { ok: true, data: await action() }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Unknown archive fault.'
      return { ok: false, data: null }
    } finally {
      loading.value = false
    }
  }

  async function fetchBooks() {
    const result = await run(async () => {
      books.value = (await booksApi.list()) ?? []
    })
    return result.ok
  }

  async function createBook(book) {
    const result = await run(() => booksApi.create(book))
    if (result.ok) await fetchBooks()
    return result.ok
  }

  async function updateBook(book) {
    const result = await run(() => booksApi.update(book))
    if (result.ok) await fetchBooks()
    return result.ok
  }

  async function updatePages(id, pagesRead) {
    const book = books.value.find((entry) => entry.id === id)
    if (!book) return false
    return updateBook({ ...book, pagesRead })
  }

  async function deleteBook(id) {
    const result = await run(() => booksApi.remove(id))
    if (result.ok) await fetchBooks()
    return result.ok
  }

  return {
    books,
    loading,
    error,
    fetchBooks,
    createBook,
    updateBook,
    updatePages,
    deleteBook,
  }
})
