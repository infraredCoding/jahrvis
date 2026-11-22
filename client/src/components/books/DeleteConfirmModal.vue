<script setup>
import BaseModal from './BaseModal.vue'

const props = defineProps({
  open: { type: Boolean, default: false },
  book: { type: Object, default: null },
})
const emit = defineEmits(['close', 'confirm'])
</script>

<template>
  <BaseModal
    :open="open"
    title="Purge Record"
    subtitle="DESTRUCTIVE ACTION // CONFIRMATION REQUIRED"
    @close="emit('close')"
  >
    <div class="space-y-4">
      <div class="border border-rust-500/50 bg-rust-500/10 px-4 py-3">
        <p class="text-sm font-semibold uppercase tracking-wide text-rust-400">Warning</p>
        <p class="mt-1 text-sm leading-relaxed text-bone-300">
          This record will be permanently erased from the archive. This action cannot be undone.
        </p>
      </div>

      <p class="text-sm text-bone-300">
        Target:
        <span class="font-semibold text-bone-100">{{ props.book?.title ?? '—' }}</span>
      </p>
    </div>

    <template #footer>
      <button type="button" class="btn-ghost" @click="emit('close')">Cancel</button>
      <button type="button" class="btn-rust" @click="emit('confirm')">Confirm Purge</button>
    </template>
  </BaseModal>
</template>
