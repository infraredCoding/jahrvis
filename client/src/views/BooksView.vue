<script setup>
import { computed, onMounted, ref } from 'vue'
import BookCard from '@/components/books/BookCard.vue'
import BookFormModal from '@/components/books/BookFormModal.vue'
import DeleteConfirmModal from '@/components/books/DeleteConfirmModal.vue'
import { useBooksStore } from '@/stores/books'

const store = useBooksStore()

const search = ref('')
const statusFilter = ref('ALL')
const formOpen = ref(false)
const editingBook = ref(null)
const deletingBook = ref(null)

const statuses = ['ALL', 'BACKLOG', 'READING', 'DONE', 'RE_READ']

onMounted(store.fetchBooks)

const filteredBooks = computed(() => {
  const term = search.value.trim().toLowerCase()

  return store.books.filter((book) => {
    const matchesStatus = statusFilter.value === 'ALL' || book.isReading === statusFilter.value
    const matchesTerm = !term || (book.title ?? '').toLowerCase().includes(term)
    return matchesStatus && matchesTerm
  })
})

const stats = computed(() => {
  const pagesRead = store.books.reduce((sum, book) => sum + (Number(book.pagesRead) || 0), 0)
  const totalPages = store.books.reduce((sum, book) => sum + (Number(book.totalPages) || 0), 0)

  return {
    total: store.books.length,
    pagesRead,
    percent: totalPages > 0 ? Math.round((pagesRead / totalPages) * 100) : 0,
    reading: store.books.filter((book) => book.isReading === 'READING').length,
    done: store.books.filter((book) => book.isReading === 'DONE').length,
  }
})

function openCreate() {
  editingBook.value = null
  formOpen.value = true
}

function openEdit(book) {
  editingBook.value = book
  formOpen.value = true
}

async function saveBook(book) {
  const saved = editingBook.value ? await store.updateBook(book) : await store.createBook(book)
  if (saved) formOpen.value = false
}

async function confirmDelete() {
  if (!deletingBook.value) return
  const removed = await store.deleteBook(deletingBook.value.id)
  if (removed) deletingBook.value = null
}
</script>

<template>
  <main class="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
    <div class="flex items-center justify-between border-b border-waste-600/60 pb-3">
      <p class="text-[10px] font-semibold uppercase tracking-[0.3em] text-bone-500">
        JAHRVIS // ARCHIVE NODE 07
      </p>
      <p class="flex items-center gap-2 text-[10px] uppercase tracking-[0.2em] text-bone-500">
        <span class="inline-block size-1.5 animate-pulse rounded-full bg-moss-500"></span>
        Link: Online
      </p>
    </div>

    <header class="relative mt-8 overflow-hidden border border-waste-600/70 bg-waste-800/60 px-6 py-8 sm:px-10">
      <div class="absolute inset-y-0 left-0 w-1.5 hazard-strip"></div>
      <p class="panel-label">Field Archive // Literature Recovery</p>
      <h1 class="mt-2 text-3xl font-bold uppercase tracking-[0.18em] text-bone-100 sm:text-5xl">
        The Archive
      </h1>
      <p class="mt-3 max-w-xl text-sm leading-relaxed text-bone-500">
        Surviving texts, catalogued and accounted for. Track what has been recovered, what remains,
        and how far into the wasteland you have read.
      </p>
    </header>

    <section class="mt-6 grid grid-cols-2 gap-3 lg:grid-cols-4" aria-label="Archive statistics">
      <div class="panel px-4 py-4">
        <p class="panel-label">Total Records</p>
        <p class="mt-2 text-2xl font-semibold text-bone-100">{{ stats.total }}</p>
      </div>
      <div class="panel px-4 py-4">
        <p class="panel-label">Pages Logged</p>
        <p class="mt-2 text-2xl font-semibold text-signal-400">{{ stats.pagesRead }}</p>
      </div>
      <div class="panel px-4 py-4">
        <p class="panel-label">Archive Completion</p>
        <p class="mt-2 text-2xl font-semibold text-moss-500">{{ stats.percent }}%</p>
      </div>
      <div class="panel px-4 py-4">
        <p class="panel-label">Active / Recovered</p>
        <p class="mt-2 text-2xl font-semibold text-rust-400">
          {{ stats.reading }}<span class="mx-1 text-bone-500">/</span>{{ stats.done }}
        </p>
      </div>
    </section>

    <section class="mt-8">
      <div class="flex flex-col gap-4 border-b border-waste-600/60 pb-4 lg:flex-row lg:items-center lg:justify-between">
        <div class="flex flex-wrap items-center gap-2">
          <button
            v-for="status in statuses"
            :key="status"
            type="button"
            class="border px-2.5 py-1.5 text-[10px] font-semibold uppercase tracking-[0.14em] transition-colors"
            :class="
              statusFilter === status
                ? 'border-signal-500 bg-signal-500/15 text-signal-400'
                : 'border-waste-600 text-bone-500 hover:border-bone-500 hover:text-bone-300'
            "
            @click="statusFilter = status"
          >
            {{ status.replace('_', ' ') }}
          </button>
        </div>

        <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
          <input
            v-model="search"
            class="field w-full py-2 text-xs sm:w-56"
            type="search"
            placeholder="Search records..."
            aria-label="Search book titles"
          />
          <button type="button" class="btn-signal whitespace-nowrap" @click="openCreate">
            + New Record
          </button>
        </div>
      </div>

      <div
        v-if="store.error"
        class="mt-5 flex flex-col gap-3 border border-rust-500/50 bg-rust-500/10 px-4 py-3 sm:flex-row sm:items-center sm:justify-between"
        role="alert"
      >
        <p class="text-sm text-rust-400">! {{ store.error }}</p>
        <button type="button" class="btn-ghost" @click="store.fetchBooks()">Retry</button>
      </div>

      <div v-if="store.loading" class="mt-10 text-center text-xs uppercase tracking-[0.3em] text-bone-500">
        Scanning archive...
      </div>

      <div
        v-else-if="filteredBooks.length === 0"
        class="panel mt-10 flex flex-col items-center gap-3 px-6 py-16 text-center"
      >
        <p class="text-lg font-semibold uppercase tracking-[0.2em] text-bone-300">No Signals Found</p>
        <p class="max-w-sm text-sm text-bone-500">
          No records match the current filters. Adjust the search or seal a new record into the archive.
        </p>
        <button type="button" class="btn-signal mt-2" @click="openCreate">+ New Record</button>
      </div>

      <div v-else class="mt-6 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        <BookCard
          v-for="book in filteredBooks"
          :key="book.id"
          :book="book"
          @edit="openEdit"
          @delete="deletingBook = $event"
          @pages="store.updatePages(book.id, $event)"
        />
      </div>
    </section>

    <footer class="mt-12 border-t border-waste-600/60 pt-4 text-[10px] uppercase tracking-[0.2em] text-bone-500">
      Archive terminal // End of transmission
    </footer>

    <BookFormModal
      :open="formOpen"
      :book="editingBook"
      @close="formOpen = false"
      @save="saveBook"
    />

    <DeleteConfirmModal
      :open="Boolean(deletingBook)"
      :book="deletingBook"
      @close="deletingBook = null"
      @confirm="confirmDelete"
    />
  </main>
</template>
