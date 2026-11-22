<script setup>
import { reactive, ref, watch } from 'vue'
import BaseModal from './BaseModal.vue'

const props = defineProps({
  open: { type: Boolean, default: false },
  book: { type: Object, default: null },
})
const emit = defineEmits(['close', 'save'])

const statuses = ['BACKLOG', 'READING', 'DONE', 'RE_READ']
const form = reactive({
  title: '',
  totalPages: 100,
  pagesRead: 0,
  isReading: 'BACKLOG',
})
const error = ref('')

watch(
  () => [props.open, props.book],
  () => {
    if (!props.open) return
    form.title = props.book?.title ?? ''
    form.totalPages = props.book?.totalPages ?? 100
    form.pagesRead = props.book?.pagesRead ?? 0
    form.isReading = props.book?.isReading ?? 'BACKLOG'
    error.value = ''
  },
  { immediate: true },
)

function submit() {
  const title = form.title.trim()
  const totalPages = Number(form.totalPages)
  const pagesRead = Number(form.pagesRead)

  if (!title) {
    error.value = 'A designation is required.'
    return
  }
  if (!Number.isInteger(totalPages) || totalPages < 1) {
    error.value = 'Total pages must be at least 1.'
    return
  }
  if (!Number.isInteger(pagesRead) || pagesRead < 0) {
    error.value = 'Pages read cannot be negative.'
    return
  }
  if (pagesRead > totalPages) {
    error.value = 'Pages read cannot exceed total pages.'
    return
  }

  emit('save', {
    ...(props.book ? { id: props.book.id } : {}),
    title,
    totalPages,
    pagesRead,
    isReading: form.isReading,
  })
}
</script>

<template>
  <BaseModal
    :open="open"
    :title="book ? 'Amend Record' : 'New Record'"
    :subtitle="book ? `ID #${book.id} // EDIT MODE` : 'ARCHIVE ENTRY // CREATE MODE'"
    @close="emit('close')"
  >
    <form id="book-form" class="space-y-4" @submit.prevent="submit">
      <div>
        <label class="field-label" for="book-title">Designation / Title</label>
        <input
          id="book-title"
          v-model="form.title"
          class="field"
          type="text"
          placeholder="e.g. The Last Library"
          maxlength="200"
        />
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="field-label" for="book-total">Total Pages</label>
          <input
            id="book-total"
            v-model.number="form.totalPages"
            class="field"
            type="number"
            min="1"
            step="1"
          />
        </div>
        <div>
          <label class="field-label" for="book-read">Pages Read</label>
          <input
            id="book-read"
            v-model.number="form.pagesRead"
            class="field"
            type="number"
            min="0"
            step="1"
          />
        </div>
      </div>

      <div>
        <label class="field-label" for="book-status">Recovery Status</label>
        <select id="book-status" v-model="form.isReading" class="field">
          <option v-for="status in statuses" :key="status" :value="status">
            {{ status.replace('_', ' ') }}
          </option>
        </select>
      </div>

      <p v-if="error" class="border-l-2 border-rust-500 bg-rust-500/10 px-3 py-2 text-xs text-rust-400">
        ! {{ error }}
      </p>
    </form>

    <template #footer>
      <button type="button" class="btn-ghost" @click="emit('close')">Abort</button>
      <button type="submit" form="book-form" class="btn-signal">
        {{ book ? 'Save Changes' : 'Seal Record' }}
      </button>
    </template>
  </BaseModal>
</template>
