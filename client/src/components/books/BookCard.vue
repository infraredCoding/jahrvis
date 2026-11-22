<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  book: { type: Object, required: true },
})
const emit = defineEmits(['edit', 'delete', 'pages'])

const statusStyles = {
  BACKLOG: 'border-waste-600 bg-waste-700/60 text-bone-300',
  READING: 'border-signal-500/50 bg-signal-500/10 text-signal-400',
  DONE: 'border-moss-500/50 bg-moss-500/10 text-moss-500',
  RE_READ: 'border-rust-500/50 bg-rust-500/10 text-rust-400',
}

const pagesInput = ref(props.book.pagesRead ?? 0)

watch(
  () => props.book.pagesRead,
  (value) => {
    pagesInput.value = value
  },
)

const progress = computed(() => {
  const total = Number(props.book.totalPages) || 0
  const read = Math.max(0, Number(props.book.pagesRead) || 0)
  return total > 0 ? Math.min(100, Math.round((read / total) * 100)) : 0
})

const barColor = computed(() => {
  if (progress.value >= 100) return 'bg-moss-500'
  if (progress.value >= 50) return 'bg-signal-500'
  return 'bg-rust-500'
})

const statusStyle = computed(
  () => statusStyles[props.book.isReading] ?? statusStyles.BACKLOG,
)

function logPages() {
  const total = Number(props.book.totalPages) || 0
  let value = Number(pagesInput.value)

  if (!Number.isFinite(value) || value < 0) value = 0
  if (total > 0) value = Math.min(value, total)

  pagesInput.value = value
  if (value !== Number(props.book.pagesRead)) emit('pages', value)
}
</script>

<template>
  <article class="panel group flex flex-col transition-colors duration-200 hover:border-signal-500/40">
    <div class="hazard-strip h-1 opacity-40 transition-opacity duration-200 group-hover:opacity-90"></div>

    <div class="flex flex-1 flex-col gap-5 p-5">
      <header class="flex items-start justify-between gap-3">
        <div class="min-w-0">
          <p class="panel-label">RECORD // {{ String(book.id).padStart(4, '0') }}</p>
          <h3 class="mt-1 truncate text-base font-semibold tracking-wide text-bone-100" :title="book.title">
            {{ book.title }}
          </h3>
        </div>
        <span
          class="shrink-0 border px-2 py-1 text-[9px] font-semibold uppercase tracking-[0.14em]"
          :class="statusStyle"
        >
          {{ (book.isReading ?? 'BACKLOG').replace('_', ' ') }}
        </span>
      </header>

      <div>
        <div class="flex items-end justify-between gap-2">
          <span class="panel-label">Recovery Progress</span>
          <span class="text-sm font-semibold text-bone-100">{{ progress }}%</span>
        </div>

        <div
          class="mt-2 h-2 w-full overflow-hidden border border-waste-600 bg-waste-950"
          role="progressbar"
          :aria-valuenow="progress"
          aria-valuemin="0"
          aria-valuemax="100"
          :aria-label="`Pages read for ${book.title}`"
        >
          <div
            class="h-full transition-[width] duration-300"
            :class="barColor"
            :style="{ width: `${progress}%` }"
          ></div>
        </div>

        <p class="mt-2 text-xs text-bone-500">
          <span class="text-bone-300">{{ book.pagesRead }}</span>
          /
          {{ book.totalPages }} pages logged
        </p>
      </div>

      <form class="flex items-end gap-2" @submit.prevent="logPages">
        <div class="min-w-0 flex-1">
          <label class="field-label" :for="`pages-${book.id}`">Log Pages Read</label>
          <input
            :id="`pages-${book.id}`"
            v-model.number="pagesInput"
            class="field py-1.5 text-xs"
            type="number"
            min="0"
            :max="book.totalPages"
            step="1"
          />
        </div>
        <button type="submit" class="btn-ghost py-1.5">Log</button>
      </form>

      <footer class="mt-auto flex gap-2 border-t border-waste-600/60 pt-4">
        <button type="button" class="btn-ghost flex-1" @click="emit('edit', book)">Edit</button>
        <button type="button" class="btn-rust flex-1" @click="emit('delete', book)">Purge</button>
      </footer>
    </div>
  </article>
</template>
