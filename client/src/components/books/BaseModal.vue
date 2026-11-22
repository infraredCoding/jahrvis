<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps({
  open: { type: Boolean, default: false },
  title: { type: String, default: '' },
  subtitle: { type: String, default: '' },
})
const emit = defineEmits(['close'])

const panel = ref(null)

function onKeydown(event) {
  if (props.open && event.key === 'Escape') emit('close')
}

watch(
  () => props.open,
  async (open) => {
    if (!open) return
    await nextTick()
    panel.value?.querySelector('input, select, button')?.focus()
  },
)

onMounted(() => document.addEventListener('keydown', onKeydown))
onBeforeUnmount(() => document.removeEventListener('keydown', onKeydown))
</script>

<template>
  <Teleport to="body">
    <div v-if="open" class="fixed inset-0 z-[100] flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/80 backdrop-blur-[2px]" @click="emit('close')"></div>

      <div
        ref="panel"
        class="panel relative z-10 max-h-[90vh] w-full max-w-lg overflow-y-auto"
        role="dialog"
        aria-modal="true"
        :aria-label="title"
      >
        <div class="hazard-strip h-1.5"></div>

        <header class="flex items-start justify-between gap-4 border-b border-waste-600/70 px-5 py-4">
          <div>
            <p class="panel-label">{{ subtitle }}</p>
            <h2 class="mt-1 text-lg font-semibold uppercase tracking-[0.12em] text-bone-100">
              {{ title }}
            </h2>
          </div>
          <button
            type="button"
            class="border border-waste-600 px-2 py-1 text-bone-500 transition-colors hover:border-bone-500 hover:text-bone-100"
            aria-label="Close dialog"
            @click="emit('close')"
          >
            ✕
          </button>
        </header>

        <div class="px-5 py-5">
          <slot />
        </div>

        <footer
          v-if="$slots.footer"
          class="flex justify-end gap-2 border-t border-waste-600/70 px-5 py-4"
        >
          <slot name="footer" />
        </footer>
      </div>
    </div>
  </Teleport>
</template>
